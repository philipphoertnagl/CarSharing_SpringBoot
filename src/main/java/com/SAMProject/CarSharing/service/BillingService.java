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

    @Autowired
    public BillingService(UserRepository userRepository, RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public ResponseEntity<?> createInvoice(Integer id, String authHeader) {
        String token = authHeader.substring(7);
        String username = TokenStorage.getUsernameForToken(token);

        if (username != null && userRepository.findByUsername(username).getRole().equals(User.Role.MANAGER)) {
            User customer = userRepository.findById(id);
            // TODO: implementation ob und welches Vehicle dem User zugewiesen ist
            BillingInvoice newInvoice = new BillingInvoice(100.00, customer.getUsername());
            processInvoice(newInvoice);
            return ResponseEntity.ok().body("A new invoice for user " + customer.getUsername() + " was created and gets processed ....");
        }
        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("There is no user I can create an invoice for");
        }
    }

    public void processInvoice(BillingInvoice billingInvoice) {
        try {
            String toJson = objectMapper.writeValueAsString(billingInvoice);
            rabbitTemplate.convertAndSend("CreateInvoiceQueue", toJson);
        } catch (JsonProcessingException e) {
            System.out.println("Problem with converting Object to JSON format (Objectmapper)");
        }
    }
}
