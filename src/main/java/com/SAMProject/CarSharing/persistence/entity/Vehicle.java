package com.SAMProject.CarSharing.persistence.entity;

public class Vehicle {
    private String name;
    private String description;
    private int id;
    private StatusDetails statusDetails;
    private String vehicleToken;

    public Vehicle() {
    }

    public Vehicle(String name, String description, StatusDetails statusDetails) {
        this.name = name;
        this.description = description;
        this.statusDetails = statusDetails;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getVehicleToken() {
        return vehicleToken;
    }

    public void setVehicleToken(String vehicleToken) {
        this.vehicleToken = vehicleToken;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", statusDetails=" + statusDetails +
                '}';
    }
}
