package com.example.carpoolbuddy;

public class Segway extends Vehicle
{
    private int range;
    private int weightCapacity;

    public int getRange() {
        return range;
    }
    public int getWeightCapacity() {
        return weightCapacity;
    }

    public void setRange(int range) {
        this.range = range;
    }
    public void setWeightCapacity(int weightCapacity) {
        this.weightCapacity = weightCapacity;
    }
}
