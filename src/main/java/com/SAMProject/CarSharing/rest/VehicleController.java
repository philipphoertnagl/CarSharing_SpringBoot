package com.SAMProject.CarSharing.rest;

import com.SAMProject.CarSharing.Entity.StatusDetails;
import com.SAMProject.CarSharing.Entity.User;
import com.SAMProject.CarSharing.Entity.Vehicle;
import com.SAMProject.CarSharing.dao.UserRepository;
import com.SAMProject.CarSharing.dao.VehicleRepository;
import com.SAMProject.CarSharing.security.TokenStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VehicleController {

    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;


    @Autowired
    public VehicleController(VehicleRepository vehicleRepository, UserRepository userRepository) {
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/api/vehicles")
    public ResponseEntity<?> registerVehicle(@RequestBody Vehicle vehicle, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String username = TokenStorage.getUsernameForToken(token);
        User user = userRepository.findByUsername(username);
        if (user != null && user.getRole() == User.Role.MANAGER) {
            if (vehicle.getName() == null || vehicle.getDescription() == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Name & Description of vehicle are a must");
            }
            Vehicle newVehicle = vehicleRepository.save(vehicle);
            System.out.println(newVehicle);
            return ResponseEntity.ok(newVehicle);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only Managers can registrate new Vehicles");
        }
    }

    @GetMapping("api/vehicles")
    public ResponseEntity<?> returnAllVehicles(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String username = TokenStorage.getUsernameForToken(token);

        if (username != null && userRepository.findByUsername(username).getRole().equals(User.Role.MANAGER)) {
            List<Vehicle> allVehicles = vehicleRepository.allVehicles();
            return ResponseEntity.ok(allVehicles);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only Managers can see all Vehicles");
        }
    }

    @PutMapping("/api/vehicles/{id}")
    public ResponseEntity<?> updateVehicle(@PathVariable Integer id, @RequestBody Vehicle updatedVehicle, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String username = TokenStorage.getUsernameForToken(token);
        User user = userRepository.findByUsername(username);
        if (user == null || user.getRole() != User.Role.MANAGER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token, something wrong (User does not exist?!)");
        }
        updatedVehicle.setId(id);
        Vehicle newUpdatedVehicle = vehicleRepository.save(updatedVehicle);
        System.out.println(newUpdatedVehicle);
        return ResponseEntity.ok().body("Vehicle: " + updatedVehicle.getName() + " updated");
    }
}
