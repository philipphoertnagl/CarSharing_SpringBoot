package com.SAMProject.CarSharing.Entity;

public class Vehicle {
    private String name;
    private String description;
    private int id;
    private StatusDetails statusDetails;

    public Vehicle() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StatusDetails getStatusDetails() {
        return statusDetails;
    }

    public void setStatusDetails(StatusDetails statusDetails) {
        this.statusDetails = statusDetails;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", statusDetails=" + statusDetails +
                '}';
    }
}
