package com.SAMProject.BillingSystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
    public class BillingService {

        private static final Logger log = LoggerFactory.getLogger(BillingService.class);

        @RabbitListener(queues = "UpdateStatusQueue")
        public void receiveUpdateStatusMessage(String message) throws InterruptedException {
            log.info("Received a new StatusMessage from a Vehicle: {}", message);
        }

        @RabbitListener(queues = "EmergencyQueue")
        public void receiveEmergencyMessage(String message) throws InterruptedException {
            log.info("A new Emergency occured! : {}", message);
        }

        @RabbitListener(queues = "CreateInvoiceQueue")
        public void receiveInvoiceMessage(String message) throws InterruptedException {
            log.info("Request for a new invoice for user ID: {}", message);
            //generateInvoice(message);
        }

        private void generateInvoice(Integer UserID) {
            String invoiceDate = String.valueOf(LocalDate.now());

        }
    }

