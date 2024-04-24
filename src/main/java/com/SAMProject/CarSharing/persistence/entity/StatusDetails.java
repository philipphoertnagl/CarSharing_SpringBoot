package com.SAMProject.CarSharing.persistence.entity;

import java.time.LocalDateTime;

public class StatusDetails {
    private double longitude;
    private double latitude;
    private LocalDateTime currentTimeStamp;
    private OccupyState occupyState;
    private String currentDriver;
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

    public String getCurrentDriver() {
        return currentDriver;
    }

    public void setCurrentDriver(String currentDriver) {
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
