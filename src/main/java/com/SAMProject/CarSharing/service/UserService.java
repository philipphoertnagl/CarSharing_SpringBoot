package com.SAMProject.CarSharing.service;

import com.SAMProject.CarSharing.dto.LoginRequest;
import com.SAMProject.CarSharing.dto.UserDTO;
import com.SAMProject.CarSharing.persistence.entity.CustomerDetails;
import com.SAMProject.CarSharing.persistence.entity.User;
import com.SAMProject.CarSharing.persistence.entity.Vehicle;
import com.SAMProject.CarSharing.persistence.repository.UserRepository;
import com.SAMProject.CarSharing.persistence.repository.UserRepositoryJakarta;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepositoryJakarta userRepositoryJakarta;


    @Autowired
    public UserService(UserRepositoryJakarta userRepositoryJakarta) {
        this.userRepositoryJakarta = userRepositoryJakarta;
    }


    public ResponseEntity<?> registerUser(User user) {
        if (user.getRole() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You need to select if the new user is a MANAGER or CUSTOMER");
        }
        User savedUser = userRepositoryJakarta.save(user);

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
        Optional<User> user = userRepositoryJakarta.findByUsername(loginRequest.getUsername());
        if (user.isPresent() && user.get().getPassword().equals(loginRequest.getPassword())) {
            String token = TokenUtil.generateToken();
            TokenStorage.storeToken(token, user.get().getUsername());
            System.out.printf("User: " + user.get().getUsername() + " logged in. UserToken: %s%n", token); //console outpu just for testing
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
        String token = authHeader.substring(7); // Assuming authHeader follows the format "Bearer <token>"
        String username = TokenStorage.getUsernameForToken(token);

        Optional<User> user = userRepositoryJakarta.findByUsername(username);
        if (user.isPresent() && user.get().getRole() == User.Role.MANAGER) {
            List<User> users = userRepositoryJakarta.findAll();

            List<UserDTO> userDTOs = new ArrayList<>();  // ohne DTO classe bekomm ich komische hibernate/jackson fehler...
            for (User u : users) {
                UserDTO dto = new UserDTO(u.getUsername(), u.getRole().toString(), u.getId());
                userDTOs.add(dto);
            }
            return ResponseEntity.ok(userDTOs);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token not permitted to see all Users (only Managers)");
        }
    }


    public ResponseEntity<?> updateUser(Integer id, User updatedUser, String authHeader) {
        String username = TokenStorage.getUsernameForToken(authHeader.substring(7));
        User authUser = userRepositoryJakarta.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token or user does not exist"));
        User existingUser = userRepositoryJakarta.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!authUser.getRole().equals(User.Role.MANAGER) && !authUser.getId().equals(existingUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot update this user. Only managers can.");
        }
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setPassword(updatedUser.getPassword());

        if (existingUser.getRole() == User.Role.CUSTOMER) {
            CustomerDetails updatedDetails = updatedUser.getCustomerDetails();
            if (updatedDetails != null) {
                if (existingUser.getCustomerDetails() != null) {
                    // update only fields when changed
                    CustomerDetails existingDetails = existingUser.getCustomerDetails();
                    existingDetails.setFirstName(updatedDetails.getFirstName());
                    existingDetails.setSurname(updatedDetails.getSurname());
                    existingDetails.setAge(updatedDetails.getAge());
                    existingDetails.setDrivingLicense(updatedDetails.getDrivingLicense());
                    existingDetails.setCcNumber(updatedDetails.getCcNumber());
                } else {
                    existingUser.setCustomerDetails(updatedDetails);
                }
            }
        }
        userRepositoryJakarta.save(existingUser);
        return ResponseEntity.ok().body("User: " + existingUser.getUsername() + " updated");
    }

}