package com.SAMProject.CarSharing.controller;

import com.SAMProject.CarSharing.persistence.entity.CustomerDetails;
import com.SAMProject.CarSharing.persistence.entity.User;
import com.SAMProject.CarSharing.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WebController {

    private final UserRepository userRepository;

    @Autowired
    public WebController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String password,
                               @RequestParam User.Role role, @RequestParam(required = false) String firstName,
                               @RequestParam(required = false) String surname, @RequestParam(required = false) Integer age,
                               @RequestParam(required = false) String drivingLicense, @RequestParam(required = false) Integer ccNumber,
                               RedirectAttributes redirectAttributes) {
        User user = new User(username, password);
        user.setRole(role);
        if (role == User.Role.CUSTOMER) {
            CustomerDetails customerDetails = new CustomerDetails(firstName, surname, age, drivingLicense, ccNumber);
            user.setCustomerDetails(customerDetails);
        }
        User savedUser = userRepository.saveOrUpdate(user);
        if (savedUser == null) {
            redirectAttributes.addFlashAttribute("message", "Failed to register user.");
            return "redirect:/register";
        }
        return "redirect:/login";
    }

}
