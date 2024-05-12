package com.SAMProject.CarSharing.controller;

import com.SAMProject.CarSharing.dto.EmergencyInfo;
import com.SAMProject.CarSharing.persistence.entity.StatusDetails;
import com.SAMProject.CarSharing.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VehicleStatusController {

    private final VehicleService vehicleService;
    @Autowired
    public VehicleStatusController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping("api/devices/{id}/status")
    public ResponseEntity<?> sendStatus(@PathVariable Integer id, @RequestBody StatusDetails statusDetails) {
        return vehicleService.sendStatus(id, statusDetails);
    }

    @PostMapping("api/devices/{id}/alarm")
    public ResponseEntity<?> sendAlarm(@PathVariable Integer id, @RequestBody EmergencyInfo emergencyInfo) { //TODO: add vehicle-token authorization
        return vehicleService.sendAlarm(id, emergencyInfo);
    }
}
