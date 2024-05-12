package com.SAMProject.CarSharing.dto;

import com.SAMProject.CarSharing.persistence.entity.EmergencyDetails;
import com.SAMProject.CarSharing.persistence.entity.StatusDetails;

public class EmergencyInfo {
    private EmergencyDetails.PriorityLevel priority;
    private String emergencyDescription;


    public EmergencyInfo(EmergencyDetails.PriorityLevel priority, String emergencyDescription) {
        this.priority = priority;
        this.emergencyDescription = emergencyDescription;
    }

    public EmergencyInfo() {
    }

    public EmergencyDetails.PriorityLevel getPriority() {
        return priority;
    }

    public void setPriority(EmergencyDetails.PriorityLevel priority) {
        this.priority = priority;
    }

    public String getEmergencyDescription() {
        return emergencyDescription;
    }

    public void setEmergencyDescription(String emergencyDescription) {
        this.emergencyDescription = emergencyDescription;
    }
}
