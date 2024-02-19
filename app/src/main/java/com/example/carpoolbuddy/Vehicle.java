package com.example.carpoolbuddy;

import java.io.Serializable;
import java.util.ArrayList;

public class Vehicle implements Serializable
{
    //fields
    private String owner;
    private String ownerUID;
    private String model;
    private int capacity;
    private String vehicleID;
    private ArrayList<String> ridersUIDs;
    private boolean open;
    private String vehicleType;
    private double basePrice;
    private int openSeats;
    private String description;

    //getters
    public String getOwner() {
        return owner;
    }
    public String getOwnerUID() {
        return ownerUID;
    }
    public String getModel() {
        return model;
    }
    public int getCapacity() {
        return capacity;
    }
    public String getVehicleID() {
        return vehicleID;
    }
    public ArrayList<String> getRidersUIDs() {
        return ridersUIDs;
    }
    public Boolean getOpen(){
        return open;
    }
    public String getVehicleType() {
        return vehicleType;
    }
    public double getBasePrice() {
        return basePrice;
    }
    public int getOpenSeats() {
        return openSeats;
    }
    public String getDescription()
    {
        return description;
    }

    //setters
    public void setOwner(String owner) {
        this.owner = owner;
    }
    public void setOwnerUID(String ownerUID) {
        this.ownerUID = ownerUID;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    public void setVehicleID(String vehicleID) {
        this.vehicleID = vehicleID;
    }
    public void setRidersUIDs(ArrayList<String> ridersUIDs) {
        this.ridersUIDs = ridersUIDs;
    }
    public void setOpen(boolean open) {
        this.open = open;
    }
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }
    public void setOpenSeats(int openSeats) {
        this.openSeats = openSeats;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }
}
