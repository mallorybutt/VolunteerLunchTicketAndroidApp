package com.ut.mallory.volunteer_lunch;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    //instance variable
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase fd;
    private DatabaseReference dr;
    private TextView date;
    private TextView v_name;
    private TextView time;
    private Button lunchButt;
    private Button logOut;
    private Volunteer v;
    private String currentDay;
    private int currentTime;
    private int clicked;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lunch_main);

        //volunteer user
        v = new Volunteer();

        //get instance of the user and authenticate user
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if(mUser == null){
            Intent intent = new Intent(getApplicationContext(), login_volunteer.class);
            startActivity(intent);
        }
        fd = FirebaseDatabase.getInstance();
        dr = FirebaseDatabase.getInstance().getReference().child("Volunteers").child(mUser.getUid());


        //id all text boxes and buttons
        date = findViewById(R.id.current_date);
        v_name = findViewById(R.id.vol_name);
        time = findViewById(R.id.current_time);
        lunchButt = findViewById(R.id.lunch_button);
        logOut = findViewById(R.id.log_out);

        //object variable to help format the dates
        final Date dateDay = new Date();

        //set the date text
        DateFormat dateFormatDay = new SimpleDateFormat("EEE, MMM d");
        date.setText(dateFormatDay.format(dateDay));

        //set the time text
        DateFormat dateFormatTime = new SimpleDateFormat("hh:mm aa");
        time.setText(dateFormatTime.format(dateDay));

        //store the current Day
        DateFormat current = new SimpleDateFormat("EEEE");
        currentDay = current.format(dateDay);

        //store the current time
        DateFormat dfTime = new SimpleDateFormat("kkmm");
        currentTime = Integer.parseInt(dfTime.format(dateDay));

        //when lunch button is clicked, keep record of when it was clicked
        //if the volunteer has not used their lunch ticket, switch the button's
        //text to used.
        lunchButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (v != null) {
                    //keeps track of day when the user clicked
                    DateFormat cTime = new SimpleDateFormat("MMdd");
                    clicked = Integer.parseInt(cTime.format(dateDay));
                    dr.child("lastClicked").setValue(clicked);
                }
            }
        });

        //method to sign out user if they click logout
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(),login_volunteer.class));
            }
        });
    }


    @Override
    public void onStart(){
        super.onStart();
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //get volunteer that is logged in
                v = dataSnapshot.getValue(Volunteer.class);
                //set the text name for the screen
                v_name.setText(v.getName());

                //get the current date in format of ints to compare with
                //the last day used
                Date dateDay = new Date();
                DateFormat cTime = new SimpleDateFormat("MMdd");
                int c = Integer.parseInt(cTime.format(dateDay));
                Boolean use = v.getLastClicked() == c;

                //call displayTicket from volunteer class
                if(v.displayTicket(currentDay,currentTime,use)){
                    lunchButt.setText("LUNCH TICKET READY TO USE");
                } else {
                    //lunch ticket cannot be used. Display last time used.
                    int month = v.getLastClicked()/100;
                    int day = v.getLastClicked()%100;
                    lunchButt.setText("LUNCH TICKET NOT AVAILABLE. USED ON "+month+"/"+day);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
