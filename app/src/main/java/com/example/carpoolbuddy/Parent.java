package com.example.carpoolbuddy;

import java.util.ArrayList;

public class Parent extends User
{
    private ArrayList<String> childrenUIDs;
    public Parent()
    {
        {
            super.setPriceMultiplier(1);
        }
    }
    public ArrayList<String> getChildrenUIDs()
    {
        return childrenUIDs;
    }
    public void setChildrenUIDs(ArrayList<String> childrenUIDs)
    {
        this.childrenUIDs = childrenUIDs;
    }
}
