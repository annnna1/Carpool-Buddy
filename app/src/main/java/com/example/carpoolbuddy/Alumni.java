package com.example.carpoolbuddy;

public class Alumni extends User
{
    private int graduateYear;
    public Alumni()
    {
        super.setPriceMultiplier(1.0);
    }
    public int getGraduateYear()
    {
        return graduateYear;

    }
    public void setGraduateYear(int graduateYear)
    {
        this.graduateYear = graduateYear;
    }
}
