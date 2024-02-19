package com.example.carpoolbuddy;

import java.util.ArrayList;

public class Student extends User
{
    private String graduatingYear;
    private ArrayList<Parent> parentUIDs;

    public Student()
    {
        super.setPriceMultiplier(0.5);
    }

    public String getGraduatingYear() {
        return graduatingYear;
    }
    public ArrayList<Parent> getParentUIDs() {
        return parentUIDs;
    }

    public void setGraduatingYear(String graduatingYear) {
        this.graduatingYear = graduatingYear;
    }
    public void setParentUIDs(ArrayList<Parent> parentUIDs) {
        this.parentUIDs = parentUIDs;
    }
}
