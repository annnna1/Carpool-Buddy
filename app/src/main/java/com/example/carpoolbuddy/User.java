package com.example.carpoolbuddy;

import java.util.ArrayList;

public class User
{
    //field
    private String uID;
    private String name;
    private String email;
    private String userType;
    private double priceMultiplier;
    private ArrayList<String> ownedVehicles;
    private int profileID;

    public User()
    {
        ownedVehicles = new ArrayList<String>();
        profileID = R.drawable.monkey_car;
    }
    //get
    public String getuID()
    {
        return uID;
    }
    public String getName()
    {
        return name;
    }
    public String getEmail()
    {
        return email;
    }
    public String getUserType()
    {
        return userType;
    }
    public double getPriceMultiplier()
    {
        return priceMultiplier;
    }
    public ArrayList<String> getOwnedVehicles()
    {
        return ownedVehicles;
    }
    public int getProfileID()
    {
        return profileID;
    }
    //set
    public void setuID(String u)
    {
        uID = u;
    }
    public void setName(String n)
    {
        name = n;
    }
    public void setEmail(String e)
    {
        email = e;
    }
    public void setUserType(String uT)
    {
        userType = uT;
    }
    public void setPriceMultiplier(double pM)
    {
        priceMultiplier = pM;
    }
    public void setOwnedVehicles(ArrayList<String> oV)
    {
        ownedVehicles = oV;
    }
    public void setProfileID(int profileID)
    {
        this.profileID = profileID;
    }
}
