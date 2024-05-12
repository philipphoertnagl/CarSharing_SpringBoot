package com.SAMProject.CarSharing.service;


import com.SAMProject.CarSharing.dto.EmergencyInfo;
import com.SAMProject.CarSharing.persistence.entity.EmergencyDetails;
import com.SAMProject.CarSharing.persistence.entity.StatusDetails;
import com.SAMProject.CarSharing.persistence.entity.User;
import com.SAMProject.CarSharing.persistence.entity.Vehicle;
import com.SAMProject.CarSharing.persistence.repository.UserRepository;
import com.SAMProject.CarSharing.persistence.repository.VehicleRepository;
import com.SAMProject.CarSharing.security.TokenStorage;
import com.SAMProject.CarSharing.security.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@Service
public class VehicleService {
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleService(UserRepository userRepository, VehicleRepository vehicleRepository) {
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public ResponseEntity<?> registerVehicle(Vehicle vehicle, String authHeader) {
        String token = authHeader.substring(7);
        String username = TokenStorage.getUsernameForToken(token);
        User user = userRepository.findByUsername(username);
        if (user != null && user.getRole() == User.Role.MANAGER) {
            if (vehicle.getName() == null || vehicle.getDescription() == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Name & Description of vehicle are a must");
            }
            Vehicle newVehicle = vehicleRepository.save(vehicle);
            System.out.println(newVehicle);
            //vehicleToken:
            String vehicleToken = TokenUtil.generateToken();
            newVehicle.setVehicleToken(vehicleToken);

            System.out.println("VehicleToken of vehicle ID " + newVehicle.getId() + " is: " + newVehicle.getVehicleToken());
            return ResponseEntity.ok(newVehicle + "\n The vehicle Token generated is : " + vehicleToken);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only Managers can registrate new Vehicles");
        }
    }


    public ResponseEntity<?> returnAllVehicles(String authHeader) {
        String token = authHeader.substring(7);
        String username = TokenStorage.getUsernameForToken(token);

        if (username != null && userRepository.findByUsername(username).getRole().equals(User.Role.MANAGER)) {
            List<Vehicle> allVehicles = vehicleRepository.allVehicles();
            return ResponseEntity.ok(allVehicles);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only Managers can see all Vehicles");
        }
    }

    public ResponseEntity<?> updateVehicle(Integer id, Vehicle updatedVehicle, String authHeader) {
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

    public ResponseEntity<?> deleteVehicle( Integer id, String authHeader) {
        String token = authHeader.substring(7);
        String username = TokenStorage.getUsernameForToken(token);
        User user = userRepository.findByUsername(username);
        if (user == null || user.getRole() != User.Role.MANAGER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token, something wrong (User does not exist?!)");
        } else {
            vehicleRepository.deleteVehicle(id);
            System.out.println("Vehicle with ID: " + id + "from List deleted");
            return ResponseEntity.ok().body("Vehicle with ID: " + id + " from List deleted");
        }
    }

}
