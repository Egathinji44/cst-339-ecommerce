// src/main/java/com/nexusstore/controller/AuthController.java
package com.nexusstore.controller;

import com.nexusstore.dto.UserRegistrationDto;
import com.nexusstore.service.UserService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;




/**
 * Controller handling authentication-related requests.
 * NO BUSINESS LOGIC - only request/response handling and view navigation.
 * 
 * @author NexusStore Team
 * @version 1.0
 */
@Controller
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    /**
     * Displays the login page.
     * @param model the Spring Model object
     * @param error optional error parameter from Spring Security
     * @param logout optional logout parameter
     * @return the login view name
     */
    @GetMapping("/login")
    public String showLoginForm(Model model, 
                                @org.springframework.lang.Nullable String error,
                                @org.springframework.lang.Nullable String logout) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully");
        }
        return "login";
    }
    
    /**
     * Displays the registration page.
     * @param model the Spring Model object
     * @return the registration view name
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "register";
    }
    
    /**
     * Processes user registration.
     * @param userDto the user registration data
     * @param bindingResult the validation binding result
     * @param model the Spring Model object
     * @return redirect to login page or back to registration on error
     */
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserRegistrationDto userDto,
                               BindingResult bindingResult,
                               Model model) {
        
        // Check for validation errors
        if (bindingResult.hasErrors()) {
            return "register";
        }
        
        // Check if username exists
        if (!userService.isUsernameAvailable(userDto.getUsername())) {
            bindingResult.rejectValue("username", "error.user", "Username already taken");
            return "register";
        }
        
        // Check if email exists
        if (!userService.isEmailAvailable(userDto.getEmail())) {
            bindingResult.rejectValue("email", "error.user", "Email already registered");
            return "register";
        }
        
        try {
            userService.registerUser(userDto);
            model.addAttribute("successMessage", "Registration successful! Please login.");
            return "login";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Registration failed: " + e.getMessage());
            return "register";
        }
    }
}