package com.example.carpoolbuddy;

public class Bicycle extends Vehicle
{
    private String bicycleType;
    private int weight;
    private int weightCapacity;

    public String getBicycleType() {
        return bicycleType;
    }
    public int getWeight() {
        return weight;
    }
    public int getWeightCapacity() {
        return weightCapacity;
    }

    public void setBicycleType(String bicycleType) {
        this.bicycleType = bicycleType;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }
    public void setWeightCapacity(int weightCapacity) {
        this.weightCapacity = weightCapacity;
    }
}
