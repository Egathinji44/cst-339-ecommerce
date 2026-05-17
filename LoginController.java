package com.nexusstore.controller;

import com.nexusstore.service.UserStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Controller
public class LoginController {
    
    private final UserStorageService userStorageService;
    
    @Autowired
    public LoginController(UserStorageService userStorageService) {
        this.userStorageService = userStorageService;
    }
    
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        if (userStorageService.authenticate(username, password)) {
            session.setAttribute("loggedInUser", username);
            userStorageService.updateLastLogin(username, LocalDateTime.now());
            redirectAttributes.addFlashAttribute("successMessage", "Welcome back, " + username + "!");
            return "redirect:/dashboard";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid username or password");
            return "redirect:/login";
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("successMessage", "You have been logged out");
        return "redirect:/login";
    }
}