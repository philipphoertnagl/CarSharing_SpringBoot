package com.SAMProject.CarSharing.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Car needs a name")
    @Column(name = "name", nullable = false, unique = false)
    private String name;
    @Column(name = "description", nullable = false, unique = false)
    private String description;

    @OneToOne
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
