package com.SAMProject.CarSharing.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "status_details")
public class StatusDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private double longitude;
    private double latitude;
    private LocalDateTime currentTimeStamp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OccupyState occupyState;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "current_driver_id")
    private User currentDriver;

    private long distanceSinceLastUpdate;
    private long timeSinceLastUpdate;       //in sec


    public enum OccupyState {
        FREE,
        OCCUPIED,
        OUT_OF_ORDER;
    }


//Für Registrierung neuer Autos
    public StatusDetails() {
        this.longitude = longitude;
        this.latitude = latitude;
        this.currentTimeStamp = LocalDateTime.now();
        this.occupyState = OccupyState.FREE;
        this.currentDriver = null;
        this.distanceSinceLastUpdate = 0;
        this.timeSinceLastUpdate = 0;
    }

    //für occupyVehicle method in VehicleService:

    public StatusDetails(OccupyState occupyState, User currentDriver) {
        this.occupyState = occupyState;
        this.currentDriver = currentDriver;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public LocalDateTime getCurrentTimeStamp() {
        return currentTimeStamp;
    }

    public void setCurrentTimeStamp(LocalDateTime currentTimeStamp) {
        this.currentTimeStamp = currentTimeStamp;
    }

    public OccupyState getOccupyState() {
        return occupyState;
    }

    public void setOccupyState(OccupyState occupyState) {
        this.occupyState = occupyState;
    }

    public User getCurrentDriver() {
        return currentDriver;
    }

    public void setCurrentDriver(User currentDriver) {
        this.currentDriver = currentDriver;
    }

    public long getDistanceSinceLastUpdate() {
        return distanceSinceLastUpdate;
    }

    public void setDistanceSinceLastUpdate(long distanceSinceLastUpdate) {
        this.distanceSinceLastUpdate = distanceSinceLastUpdate;
    }

    public long getTimeSinceLastUpdate() {
        return timeSinceLastUpdate;
    }

    public void setTimeSinceLastUpdate(long timeSinceLastUpdate) {
        this.timeSinceLastUpdate = timeSinceLastUpdate;
    }

    @Override
    public String toString() {
        return "StatusDetails{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                ", currentTimeStamp=" + currentTimeStamp +
                ", occupyState=" + occupyState +
                ", currentDriver='" + currentDriver + '\'' +
                ", distanceSinceLastUpdate=" + distanceSinceLastUpdate +
                ", timeSinceLastUpdate=" + timeSinceLastUpdate +
                '}';
    }
}
