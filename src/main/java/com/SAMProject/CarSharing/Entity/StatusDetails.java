package com.SAMProject.CarSharing.Entity;

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

    public StatusDetails(double longitude, double latitude, LocalDateTime currentTimeStamp, OccupyState occupyState, String currentDriver, long distanceSinceLastUpdate, long timeSinceLastUpdate) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.currentTimeStamp = currentTimeStamp;
        this.occupyState = occupyState;
        this.currentDriver = currentDriver;
        this.distanceSinceLastUpdate = distanceSinceLastUpdate;
        this.timeSinceLastUpdate = timeSinceLastUpdate;
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
