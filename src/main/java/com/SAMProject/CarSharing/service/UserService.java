package com.SAMProject.CarSharing.service;

import com.SAMProject.CarSharing.dto.LoginRequest;
import com.SAMProject.CarSharing.persistence.entity.CustomerDetails;
import com.SAMProject.CarSharing.persistence.entity.User;
import com.SAMProject.CarSharing.persistence.entity.Vehicle;
import com.SAMProject.CarSharing.persistence.repository.UserRepository;
import com.SAMProject.CarSharing.security.TokenStorage;
import com.SAMProject.CarSharing.security.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Collections;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public ResponseEntity<?> registerUser(User user) {
        if (user.getRole() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You need to select if the new user is a MANAGER or CUSTOMER");
        }
        User savedUser = userRepository.saveOrUpdate(user);

        if (savedUser.getRole() == User.Role.CUSTOMER) {
            System.out.println("New Customer registered: "
                    + savedUser.getUsername()
                    + " Details: " + savedUser.getCustomerDetails().toString());

        } else if (savedUser.getRole() == User.Role.MANAGER) {
            System.out.println("New Fleet Manager registered: "
                    + " Username: " + savedUser.getUsername()
                    + " Password: " + savedUser.getPassword());
        }
        return ResponseEntity.ok(savedUser);
    }

    public ResponseEntity<?> loginUser(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername());
        if (user != null && user.getPassword().equals(loginRequest.getPassword())) {
            String token = TokenUtil.generateToken();
            TokenStorage.storeToken(token, user.getUsername());
            System.out.printf("User: " + user.getUsername() + " logged in. UserToken: %s%n", token); //console outpu just for testing
            return ResponseEntity.ok().body(Collections.singletonMap("token", token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    public ResponseEntity<?> logoutUser(String authHeader) {
        //token sent in the authorization header  with "Bearer "
        String token = authHeader.substring(7); // Remove "Bearer " prefix
        String username = TokenStorage.getUsernameForToken(token);
        if (username != null) {
            System.out.println("Logging out user: " + username + " with Token: " + token);
            TokenStorage.removeToken(token);
            return ResponseEntity.ok().body("User: " + username + " logged out successfully");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token or user already logged out");
        }
    }

    public ResponseEntity<?> returnAllUser(String authHeader) {
        String token = authHeader.substring(7);
        String username = TokenStorage.getUsernameForToken(token);

        if (username != null && userRepository.findByUsername(username).getRole().equals(User.Role.MANAGER)) {
            List<User> users = userRepository.allUsers();
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }


    public ResponseEntity<?> updateUser(Integer id, User updatedUser, String authHeader) {
        String token = authHeader.substring(7);
        String username = TokenStorage.getUsernameForToken(token);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token, something wrong (User does not exist?!)");
        }
        User existingUser = userRepository.findById(id);
        updatedUser.setId(id);  //!wichtig!
        updatedUser.setRole(existingUser.getRole());
        // Save CustomerDetails of updated Customer:
        CustomerDetails updatedDetails = updatedUser.getCustomerDetails(); //to not have to send the role info in the JSON body again
        if (user.getRole().equals(User.Role.MANAGER) || user.getId() == id) {
            if (updatedUser.getRole() == User.Role.CUSTOMER) {
                updatedUser.setCustomerDetails(updatedDetails); //set customerDetails from updated Customer
            }
            userRepository.saveOrUpdate(updatedUser);
            return ResponseEntity.ok().body("User: " + updatedUser.getUsername() + " updated");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot update this user.(Only manager can change other users data");
        }
    }

}