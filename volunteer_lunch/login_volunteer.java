package com.ut.mallory.volunteer_lunch;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login_volunteer extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button l_btn;
    private EditText emailEdit;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_volunteer);

        //define instance variables
        l_btn = (Button) findViewById(R.id.login);
        emailEdit = (EditText) findViewById(R.id.enter_email);

        mAuth = FirebaseAuth.getInstance();

        //sign in attempt
        l_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = emailEdit.getText().toString();
                signIn(userEmail);
            }
        });

    }

    //class to sign in a user
    public void signIn (String e){
        mAuth.signInWithEmailAndPassword(e, "123456").addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            //after button is clicked
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    //go to main activity
                    Intent userIn = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(userIn);
                } else {
                    //show failure
                    Toast.makeText(login_volunteer.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }





}

