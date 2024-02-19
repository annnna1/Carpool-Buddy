package com.example.carpoolbuddy;

public class Teacher extends User
{
    private String inSchoolTitle;
    public Teacher()
    {
        super.setPriceMultiplier(0.5);
    }
    public void setInSchoolTitle(String inSchoolTitle) {
        this.inSchoolTitle = inSchoolTitle;
    }
    public String getInSchoolTitle() {
        return inSchoolTitle;
    }
}
