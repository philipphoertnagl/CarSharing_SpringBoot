package com.SAMProject.CarSharing.persistence.entity;

public class BillingInvoice {

    private double totalPrice;
    private String username;

    public BillingInvoice(double totalPrice, String username) {
        this.totalPrice = totalPrice;
        this.username = username;
    }


    //TODO: methods for using the statusDetails information will come...
    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
