package com.nexusstore.controller;

import com.nexusstore.model.User;
import com.nexusstore.service.UserStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import java.util.Objects;

@Controller
public class RegistrationController {
    
    private final UserStorageService userStorageService;
    
    @Autowired
    public RegistrationController(UserStorageService userStorageService) {
        this.userStorageService = userStorageService;
    }
    
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
    
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "register";
        }
        try {
            userStorageService.save(user);
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please login.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            String errorMsg = Objects.requireNonNull(Objects.toString(e.getMessage(), "Registration failed"));
            result.rejectValue("username", "error.user", errorMsg);
            return "register";
        }
    }
}