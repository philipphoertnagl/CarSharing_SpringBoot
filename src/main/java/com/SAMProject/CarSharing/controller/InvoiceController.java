package com.SAMProject.CarSharing.controller;

import com.SAMProject.CarSharing.persistence.entity.Vehicle;
import com.SAMProject.CarSharing.service.BillingService;
import com.SAMProject.CarSharing.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class InvoiceController {

    private final BillingService billingService;
    @Autowired
    public InvoiceController(BillingService billingService) {
        this.billingService = billingService;
    }

    @PostMapping("/api/invoices/{id}")
    public ResponseEntity<?> createInvoice(@PathVariable Integer id, @RequestHeader("Authorization") String authHeader) {
        return billingService.createInvoice(id, authHeader);
    }

}
