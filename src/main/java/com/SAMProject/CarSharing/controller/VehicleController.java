package com.SAMProject.CarSharing.controller;

import com.SAMProject.CarSharing.dto.UsernameOccupyVehicle;
import com.SAMProject.CarSharing.persistence.entity.Vehicle;
import com.SAMProject.CarSharing.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class VehicleController {

    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping("/api/vehicles")
    public ResponseEntity<?> registerVehicle(@RequestBody Vehicle vehicle, @RequestHeader("Authorization") String authHeader) {
        return vehicleService.registerVehicle(vehicle, authHeader);
    }

    @GetMapping("api/vehicles")
    public ResponseEntity<?> returnAllVehicles(@RequestHeader("Authorization") String authHeader) {
        return vehicleService.returnAllVehicles(authHeader);
    }

    @PutMapping("/api/vehicles/{id}")
    public ResponseEntity<?> updateVehicle(@PathVariable Integer id, @RequestBody Vehicle updatedVehicle, @RequestHeader("Authorization") String authHeader) {
        return vehicleService.updateVehicle(id, updatedVehicle, authHeader);
    }

    @DeleteMapping("api/vehicles/{id}")
    public ResponseEntity<?> deleteVehicle(@PathVariable Integer id, @RequestHeader("Authorization") String authHeader) {
        return vehicleService.deleteVehicle(id, authHeader);
    }

    @PostMapping("api/vehicles/{vehicleID}")
    public ResponseEntity<?> occupyVehicle(@PathVariable Integer vehicleID, @RequestBody UsernameOccupyVehicle request) {
        return vehicleService.occupyVehicle(vehicleID, request.getUsername());
    }
}
