package com.SAMProject.CarSharing.dto;

public class LoginRequest {         // wird gebraucht, um nicht bei Login nochmal Role mitzusenden
    private String username;
    private String password;


    public LoginRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}