package com.SAMProject.CarSharing.controller;

import com.SAMProject.CarSharing.dto.EmergencyInfo;
import com.SAMProject.CarSharing.persistence.entity.StatusDetails;
import com.SAMProject.CarSharing.service.VehicleService;
import com.SAMProject.CarSharing.service.VehicleStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class VehicleStatusController {

    private final VehicleStatusService vehicleStatusService;

    @Autowired
    public VehicleStatusController(VehicleStatusService vehicleStatusService) {
        this.vehicleStatusService = vehicleStatusService;
    }

    @PostMapping("api/devices/{id}/status")
    public ResponseEntity<?> sendStatus(@PathVariable Integer id, @RequestBody StatusDetails statusDetails, @RequestHeader("Authorization") String authHeader) {
        return vehicleStatusService.sendStatus(id, statusDetails, authHeader);
    }

    @PostMapping("api/devices/{id}/alarm")
    public ResponseEntity<?> sendAlarm(@PathVariable Integer id, @RequestBody EmergencyInfo emergencyInfo, @RequestHeader("Authorization") String authHeader) {
        return vehicleStatusService.sendAlarm(id, emergencyInfo, authHeader);
    }
}
