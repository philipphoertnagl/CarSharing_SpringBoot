package com.SAMProject.CarSharing.controller;

import com.SAMProject.CarSharing.persistence.entity.CustomerDetails;
import com.SAMProject.CarSharing.persistence.entity.User;
import com.SAMProject.CarSharing.dto.LoginRequest;
import com.SAMProject.CarSharing.security.TokenStorage;
import com.SAMProject.CarSharing.security.TokenUtil;
import com.SAMProject.CarSharing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
public class UserController {

    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/users/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/api/users/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        return userService.loginUser(loginRequest);
    }

    @PostMapping("/api/users/logout")
    public ResponseEntity<?> logoutUser(@RequestHeader("Authorization") String authHeader) {
        return userService.logoutUser(authHeader);
    }

    @GetMapping("/api/users")
    public ResponseEntity<?> returnAllUser(@RequestHeader("Authorization") String authHeader) {
        return userService.returnAllUser(authHeader);
    }

    @PutMapping("api/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody User updatedUser, @RequestHeader("Authorization") String authHeader) {
        return userService.updateUser(id, updatedUser, authHeader);
    }

}
