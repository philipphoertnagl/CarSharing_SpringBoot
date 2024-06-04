package com.SAMProject.CarSharing.controller;

import com.SAMProject.CarSharing.persistence.entity.User;
import com.SAMProject.CarSharing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WebController {

    private final UserService userService;

    @Autowired
    public WebController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUserForm(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        ResponseEntity<?> response = userService.registerUser(user);
        if (!response.getStatusCode().is2xxSuccessful()) {
            redirectAttributes.addFlashAttribute("error", response.getBody());
            return "redirect:/register";
        }
        redirectAttributes.addFlashAttribute("username", user.getUsername());
        redirectAttributes.addFlashAttribute("success", "Successfully registered " + user.getUsername());
        return "redirect:/registersuccess";
    }

    @GetMapping("/registersuccess")
    public String registrationSuccess() {
        return "registersuccess";
    }


}
