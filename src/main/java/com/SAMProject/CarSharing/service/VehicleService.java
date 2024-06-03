package com.SAMProject.CarSharing.service;


import com.SAMProject.CarSharing.dto.EmergencyInfo;
import com.SAMProject.CarSharing.dto.VehicleDTO;
import com.SAMProject.CarSharing.persistence.entity.EmergencyDetails;
import com.SAMProject.CarSharing.persistence.entity.StatusDetails;
import com.SAMProject.CarSharing.persistence.entity.User;
import com.SAMProject.CarSharing.persistence.entity.Vehicle;
import com.SAMProject.CarSharing.persistence.repository.UserRepository;
import com.SAMProject.CarSharing.persistence.repository.UserRepositoryJakarta;
import com.SAMProject.CarSharing.persistence.repository.VehicleRepository;
import com.SAMProject.CarSharing.persistence.repository.VehicleRepositoryJakarta;
import com.SAMProject.CarSharing.security.TokenStorage;
import com.SAMProject.CarSharing.security.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {
    private final UserRepositoryJakarta userRepositoryJakarta;
    private final VehicleRepositoryJakarta vehicleRepositoryJakarta;

    @Autowired
    public VehicleService(UserRepositoryJakarta userRepositoryJakarta, VehicleRepositoryJakarta vehicleRepositoryJakarta) {
        this.userRepositoryJakarta = userRepositoryJakarta;
        this.vehicleRepositoryJakarta = vehicleRepositoryJakarta;
    }

    public ResponseEntity<?> registerVehicle(Vehicle vehicle, String authHeader) {
        String token = authHeader.substring(7);
        String username = TokenStorage.getUsernameForToken(token);
        Optional<User> user = userRepositoryJakarta.findByUsername(username);
        if (user.isPresent() && user.get().getRole() == User.Role.MANAGER) {
            if (vehicle.getName() == null || vehicle.getDescription() == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Name & Description of vehicle are a must");
            }
            Vehicle newVehicle = vehicleRepositoryJakarta.save(vehicle);
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
        String token = authHeader.substring(7);  // Assuming authHeader follows the format "Bearer <token>"
        String username = TokenStorage.getUsernameForToken(token);

        Optional<User> user = userRepositoryJakarta.findByUsername(username);
        if (user.isPresent() && user.get().getRole() == User.Role.MANAGER) {
            List<Vehicle> vehicles = vehicleRepositoryJakarta.findAll();

            List<VehicleDTO> vehicleDTOs = new ArrayList<>();
            for (Vehicle vehicle : vehicles) {
                String status = vehicle.getStatusDetails() != null ? vehicle.getStatusDetails().getOccupyState().toString() : "UNAVAILABLE";
                VehicleDTO dto = new VehicleDTO( vehicle.getName(), vehicle.getDescription(), status, vehicle.getId());
                vehicleDTOs.add(dto);
            }
            return ResponseEntity.ok(vehicleDTOs);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token not permitted to see all Vehicles (only Managers)");
        }
    }


    public ResponseEntity<?> updateVehicle(Integer id, Vehicle updatedVehicle, String authHeader) {
        String token = authHeader.substring(7);
        String username = TokenStorage.getUsernameForToken(token);
        Optional<User> user = userRepositoryJakarta.findByUsername(username);
        if (user.isEmpty() || user.get().getRole() != User.Role.MANAGER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token, something wrong (User does not exist?!)");
        }
        /*
        updatedVehicle.setId(id);
        Vehicle newUpdatedVehicle = vehicleRepository.save(updatedVehicle);
        System.out.println(newUpdatedVehicle);
         */

        Vehicle existingVehicle = vehicleRepositoryJakarta.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found"));

        existingVehicle.setName(updatedVehicle.getName());
        existingVehicle.setDescription(updatedVehicle.getDescription());
        vehicleRepositoryJakarta.save(existingVehicle);
        return ResponseEntity.ok().body("Vehicle: " + updatedVehicle.getName() + " updated");
    }

    public ResponseEntity<?> deleteVehicle( Integer id, String authHeader) {
        String token = authHeader.substring(7);
        String username = TokenStorage.getUsernameForToken(token);
        Optional<User> user = userRepositoryJakarta.findByUsername(username);
        if (user.isEmpty() || user.get().getRole() != User.Role.MANAGER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token, something wrong (User does not exist?!)");
        } else {
            vehicleRepositoryJakarta.deleteById(id);
            System.out.println("Vehicle with ID: " + id + "from List deleted");
            return ResponseEntity.ok().body("Vehicle with ID: " + id + " from List deleted");
        }
    }


    public ResponseEntity<?> occupyVehicle(Integer vehicleID, String username) {
        //check for the User who wants to select a vehicle
        Optional<User> user = userRepositoryJakarta.findByUsername(username); // because of JSON parsing as raw String needed a new DTO
        //TODO: check if username is a Customer and logged in (Should Managers be excluded of selecting vehicles?)
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid ID - this user does not exist)");
        }
        //figure out which vehicle he wants to choose (by entered vehicleID as path parameter)
        Optional<Vehicle> vehicle = vehicleRepositoryJakarta.findById(vehicleID);
        if (vehicle.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle ID not found");
        } else {
            //process so that other fields of statusDetails do not get overwritten or set to 0 / NULL
            StatusDetails existingStatusDetails = vehicle.get().getStatusDetails();
            existingStatusDetails.setOccupyState(StatusDetails.OccupyState.OCCUPIED);
            existingStatusDetails.setCurrentDriver(user.get());
            vehicle.get().setStatusDetails(existingStatusDetails);
            vehicleRepositoryJakarta.save(vehicle.get()); //eig nicht gebraucht, aber vlt später wenn DBS
        }
        System.out.println(user.get().getUsername() + " rented the car " + vehicle.get().getName() + " with ID " + vehicle.get().getId() + " and the details " + vehicle.get().getStatusDetails());
        return ResponseEntity.ok().body("Vehicle with ID: " + vehicleID + " was rented by User " + user.get().getUsername() + " wih User ID: " + user.get().getId());
    }

}
