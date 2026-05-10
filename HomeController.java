package com.nexusstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        model.addAttribute("currentYear", LocalDateTime.now().getYear());
        
        String loggedInUser = (String) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            model.addAttribute("loggedInUser", loggedInUser);
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
        model.addAttribute("currentDate", LocalDateTime.now().format(formatter));
        
        return "index";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        String loggedInUser = (String) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("currentYear", LocalDateTime.now().getYear());
        return "dashboard";
    }
    
    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("currentYear", LocalDateTime.now().getYear());
        return "about";
    }
}