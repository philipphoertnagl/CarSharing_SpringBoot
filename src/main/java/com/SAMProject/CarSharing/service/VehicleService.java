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


    public ResponseEntity<?> occupyVehicle(Integer vehicleID, String username) {
        //check for the User who wants to select a vehicle
        User user = userRepository.findByUsername(username); // because of JSON parsing as raw String needed a new DTO
        //TODO: check if username is a Customer and logged in (Should Managers be excluded of selecting vehicles?)
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid ID - this user does not exist)");
        }
        //figure out which vehicle he wants to choose (by entered vehicleID as path parameter)
        Vehicle vehicle = vehicleRepository.findById(vehicleID);
        if (vehicle == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle ID not found");
        } else {
            //process so that other fields of statusDetails do not get overwritten or set to 0 / NULL
            StatusDetails existingStatusDetails = vehicle.getStatusDetails();
            existingStatusDetails.setOccupyState(StatusDetails.OccupyState.OCCUPIED);
            existingStatusDetails.setCurrentDriver(user);
            vehicle.setStatusDetails(existingStatusDetails);
            vehicleRepository.save(vehicle); //eig nicht gebraucht, aber vlt später wenn DBS
        }
        System.out.println(user.getUsername() + " rented the car " + vehicle.getName() + " with ID " + vehicle.getId() + " and the details " + vehicle.getStatusDetails());
        return ResponseEntity.ok().body("Vehicle with ID: " + vehicleID + " was rented by User " + user.getUsername() + " wih User ID: " + user.getId());
    }

}
