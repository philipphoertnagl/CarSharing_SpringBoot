package com.SAMProject.BillingSystem.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String UPDATE_STATUS_QUEUE = "UpdateStatusQueue";
    public static final String EMERGENCY_QUEUE = "EmergencyQueue";
    public static final String CREATE_INVOICE_QUEUE = "CreateInvoiceQueue";

    @Bean
    public Queue updateStatusQueue() {
        return new Queue(UPDATE_STATUS_QUEUE, true);
    }

    @Bean
    public Queue emergencyQueue() {
        return new Queue(EMERGENCY_QUEUE, false);
    }

    @Bean
    Queue createInvoiceQueue() {
        return new Queue(CREATE_INVOICE_QUEUE, false);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setDefaultReceiveQueue(UPDATE_STATUS_QUEUE);
        return rabbitTemplate;
    }


}
