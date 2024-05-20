package com.SAMProject.CarSharing.service;

import com.SAMProject.CarSharing.persistence.entity.BillingInvoice;
import com.SAMProject.CarSharing.persistence.entity.StatusDetails;
import com.SAMProject.CarSharing.persistence.entity.User;
import com.SAMProject.CarSharing.persistence.entity.Vehicle;
import com.SAMProject.CarSharing.persistence.repository.UserRepository;
import com.SAMProject.CarSharing.persistence.repository.VehicleRepository;
import com.SAMProject.CarSharing.security.TokenStorage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BillingService {
    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper; //for RabbitMQ SimpleMessageConverter to convert to simple JSON
    private final VehicleRepository vehicleRepository;

    @Autowired
    public BillingService(UserRepository userRepository, RabbitTemplate rabbitTemplate, ObjectMapper objectMapper, VehicleRepository vehicleRepository) {
        this.userRepository = userRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.vehicleRepository = vehicleRepository;
    }

    public ResponseEntity<?> createInvoice(Integer id, String authHeader) {
        String token = authHeader.substring(7);
        String username = TokenStorage.getUsernameForToken(token);
        if (username != null && userRepository.findByUsername(username).getRole().equals(User.Role.MANAGER)) {
            User customer = userRepository.findById(id);
            if (customer != null) {
                Vehicle vehicleDetails = vehicleRepository.findVehicleByUserID(customer.getId()); // check if customer has selected a vehicle
                if (vehicleDetails != null && vehicleDetails.getStatusDetails() != null) {
                    StatusDetails vehicleStatusDetails = vehicleDetails.getStatusDetails();
                    processInvoice(vehicleStatusDetails); //sending to the InvoiceQUEUE
                    return ResponseEntity.ok().body("A new invoice for user " + customer.getUsername() + " with User ID "
                            + customer.getId() + " was created and gets processed ....");
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No vehicle or status details found for the user");
                }

            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Such a user is not in the database");
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not a MANAGER!");
        }
    }


    public void processInvoice(StatusDetails statusDetails){
            try {
                String toJson = objectMapper.writeValueAsString(statusDetails);
                rabbitTemplate.convertAndSend("CreateInvoiceQueue", toJson);
            } catch (JsonProcessingException e) {
                System.out.println("Problem with converting Object to JSON format (Objectmapper)");
            }
        }
    }



