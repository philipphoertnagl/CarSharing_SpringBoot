package com.SAMProject.CarSharing.service;

import com.SAMProject.CarSharing.dto.EmergencyInfo;
import com.SAMProject.CarSharing.persistence.entity.BillingInvoice;
import com.SAMProject.CarSharing.persistence.entity.EmergencyDetails;
import com.SAMProject.CarSharing.persistence.entity.StatusDetails;
import com.SAMProject.CarSharing.persistence.entity.Vehicle;
import com.SAMProject.CarSharing.persistence.repository.VehicleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class VehicleStatusService {

    private final VehicleRepository vehicleRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper; //for RabbitMQ SimpleMessageConverter to convert to simple JSON

    @Autowired
    public VehicleStatusService(VehicleRepository vehicleRepository, RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.vehicleRepository = vehicleRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public ResponseEntity<?> sendStatus(Integer id, StatusDetails newStatusDetails, String authHeader) {
        String vehicleToken = authHeader.substring(7);
        Vehicle vehicle = vehicleRepository.findById(id);
        if (vehicle == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle ID not found");
        }
        if(!vehicle.getVehicleToken().equals(vehicleToken)) {       //checking for matching VehicleToken!
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Vehicle Token not matching with Token of VehicleID");
        }
        StatusDetails existingStatusDetails = vehicle.getStatusDetails();
        if (existingStatusDetails != null) {
            newStatusDetails.setOccupyState(existingStatusDetails.getOccupyState());
            newStatusDetails.setCurrentDriver(existingStatusDetails.getCurrentDriver());
        }
        vehicle.setStatusDetails(newStatusDetails);
        processStatusUpdate(newStatusDetails);

        System.out.println("Vehicle: " + vehicle.getName() + " has new Status: " + vehicle.getStatusDetails());
        return ResponseEntity.ok().body("Vehicle: " + vehicle.getName() + " has new Status: " + vehicle.getStatusDetails());
    }

    public ResponseEntity<?> sendAlarm(Integer id, EmergencyInfo emergencyInfo, String authHeader) {
        String vehicleToken = authHeader.substring(7);
        Vehicle vehicle = vehicleRepository.findById(id);
        if (vehicle == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle ID not found");
        }
        if(!vehicle.getVehicleToken().equals(vehicleToken)) {       //checking for matching VehicleToken!
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Vehicle Token not matching with Token of VehicleID");
        }
        StatusDetails currentStatus = vehicle.getStatusDetails();
        EmergencyDetails emergencyDetails = new EmergencyDetails(currentStatus, emergencyInfo.getPriority(), emergencyInfo.getEmergencyDescription());
        processEmergencyAlarm(emergencyDetails);
        System.out.println("Emergency Details sent: " + emergencyDetails);
        return ResponseEntity.ok().body("Emergency Details sent: " + emergencyDetails);
    }

    public void processStatusUpdate(StatusDetails statusDetails) {
        try {
            String toJson = objectMapper.writeValueAsString(statusDetails); //for SimpleMessageConverter in RabbitMQ
            rabbitTemplate.convertAndSend("UpdateStatusQueue", toJson);
        } catch (JsonProcessingException e) {
            System.out.println("Problem with converting Object to JSON format (Objectmapper)");
        }


    }

    public void processEmergencyAlarm(EmergencyDetails emergencyDetails) {
        try {
            String toJson = objectMapper.writeValueAsString(emergencyDetails);
            rabbitTemplate.convertAndSend("EmergencyQueue", toJson);
        } catch (JsonProcessingException e) {
            System.out.println("Problem with converting Object to JSON format (Objectmapper)");
        }
    }

}
