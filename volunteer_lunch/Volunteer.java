package com.ut.mallory.volunteer_lunch;

import java.util.ArrayList;
import java.util.Map;

public class Volunteer {

    private Map<String, ArrayList<Integer>> shifts;
    private String name;
    private String email;
    private int lastClicked;


    //no argument constructor
    public Volunteer(){

    }

    //constructor
    public Volunteer(String n, String e, Map<String,ArrayList<Integer>> s, int lastClick){
        name = n;
        email = e;
        shifts = s;
        lastClicked=lastClick;
    }

    //classic getter
    public String getName(){
        return name;
    }

    //classic getter
    public String getEmail(){
        return email;
    }

    //classic getter
    public Map getShifts(){
        return shifts;
    }

    //classic getter
    public int getLastClicked(){
        return lastClicked;
    }

    //method to determine to display the ticket as available
    //pre: pass in the current day and time and the last time the volunteer
    //     used their lunch ticket
    //post: return true if lunch ticket is available. False otherwise.
    public Boolean displayTicket(String currentDay, int currentTime, boolean used){
        if(checkShiftDay(currentDay) && checkShiftTime(currentTime, currentDay)){
            //in the shift
            if(!used){
                return true;
            }
        }
        return false;
    }

    //method to compare current with the shift day
    //pre: pass the current day
    //post: return true is the current day matches a shift day
    public Boolean checkShiftDay(String currentDay){
        return shifts.containsKey(currentDay);
    }


    //method to check if a volunteer is in their shift by time.
    //pre: pass the current time and day
    //post: return true if in a shift
    public boolean checkShiftTime(int currentTime, String currentDay) {
        int startTime = shifts.get(currentDay).get(0);
        int finishTime = shifts.get(currentDay).get(1);
        if(currentTime >=  startTime){
            if(currentTime <= finishTime){
                //in shift
                return true;
            }
        }
        return false;
    }



}
