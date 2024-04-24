package com.SAMProject.CarSharing.service;

import com.SAMProject.CarSharing.persistence.entity.User;
import com.SAMProject.CarSharing.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


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
}