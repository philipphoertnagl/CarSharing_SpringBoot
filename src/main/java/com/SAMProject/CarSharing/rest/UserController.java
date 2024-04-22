package com.SAMProject.CarSharing.rest;

import com.SAMProject.CarSharing.persistence.entity.CustomerDetails;
import com.SAMProject.CarSharing.persistence.entity.User;
import com.SAMProject.CarSharing.persistence.repository.UserRepository;
import com.SAMProject.CarSharing.dto.LoginRequest;
import com.SAMProject.CarSharing.security.TokenStorage;
import com.SAMProject.CarSharing.security.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
public class UserController {
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/api/users/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
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

    @PostMapping("/api/users/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
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

    @PostMapping("/api/users/logout")
    public ResponseEntity<?> logoutUser(@RequestHeader("Authorization") String authHeader) {
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

    @GetMapping("/api/users")
    public ResponseEntity<?> returnAllUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String username = TokenStorage.getUsernameForToken(token);

        if (username != null && userRepository.findByUsername(username).getRole().equals(User.Role.MANAGER)) {
            List<User> users = userRepository.allUsers();
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("api/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody User updatedUser, @RequestHeader("Authorization") String authHeader) {
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
