package com.SAMProject.CarSharing.persistence.entity;

import com.SAMProject.CarSharing.dto.EmergencyInfo;

public class EmergencyDetails {
    private StatusDetails statusDetails;
    private PriorityLevel priority;
    private String emergencyDescription;

    public enum PriorityLevel {
        LOW,
        MID,
        HIGH;
    }

    public EmergencyDetails(StatusDetails statusDetails, PriorityLevel priority, String emergencyDescription) {
        this.statusDetails = statusDetails;
        this.priority = priority;
        this.emergencyDescription = emergencyDescription;
    }

    public StatusDetails getStatusDetails() {
        return statusDetails;
    }

    public void setStatusDetails(StatusDetails statusDetails) {
        this.statusDetails = statusDetails;
    }

    public PriorityLevel getPriority() {
        return priority;
    }

    public void setPriority(PriorityLevel priority) {
        this.priority = priority;
    }

    public String getEmergencyDescription() {
        return emergencyDescription;
    }

    public void setEmergencyDescription(String emergencyDescription) {
        this.emergencyDescription = emergencyDescription;
    }

    @Override
    public String toString() {
        return "EmergencyDetails{" +
                "statusDetails=" + statusDetails +
                ", priority=" + priority +
                ", emergencyDescription='" + emergencyDescription + '\'' +
                '}';
    }
}
