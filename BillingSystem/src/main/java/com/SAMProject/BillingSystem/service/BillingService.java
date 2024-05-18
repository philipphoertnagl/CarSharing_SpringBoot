package com.SAMProject.BillingSystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class BillingService {

    private static final Logger log = LoggerFactory.getLogger(BillingService.class);

    @RabbitListener(queues = "UpdateStatusQueue")
    public void receiveUpdateStatusMessage(String message) throws InterruptedException {
        Thread.sleep(5000); //for debuggin in RabbitMQ server interface (check delivery)
        log.info("Received message from UpdateStatusQueue: {}", message);
    }

    @RabbitListener(queues = "EmergencyQueue")
    public void receiveEmergencyMessage(String message) throws InterruptedException {
        log.info("Received message from EmergencyQueue: {}", message);
    }
}

