package com.SAMProject.BillingSystem.service;

import com.SAMProject.BillingSystem.entity.StatusDetails;
import com.SAMProject.BillingSystem.util.InvoiceWriter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
    public class BillingService {

    private static final Logger log = LoggerFactory.getLogger(BillingService.class);
    private final ObjectMapper objectMapper;

    @Autowired
    public BillingService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

        @RabbitListener(queues = "UpdateStatusQueue")
        public void receiveUpdateStatusMessage(String message) throws InterruptedException {
            log.info("Received a new StatusMessage from a Vehicle: {}", message);
        }

        @RabbitListener(queues = "EmergencyQueue")
        public void receiveEmergencyMessage(String message) throws InterruptedException {
            log.info("A new Emergency occured! : {}", message);
        }

        @RabbitListener(queues = "CreateInvoiceQueue")
        public void receiveInvoiceMessage(String message) throws InterruptedException, JsonProcessingException {
            log.info("Request for a new invoice for user ID: {}", message);
            StatusDetails statusDetails = objectMapper.readValue(message, StatusDetails.class);
            String invoiceContent = InvoiceWriter.generateInvoice(statusDetails);
            InvoiceWriter.saveInvoiceToFile(invoiceContent, statusDetails);
        }


    }

