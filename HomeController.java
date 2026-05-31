package com.nexusstore.controller;

import com.nexusstore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for home page and general navigation.
 * 
 * @author NexusStore Team
 * @version 1.0
 */
@Controller
public class HomeController {
    
    @Autowired
    private ProductService productService;
    
    /**
     * Displays the home page with featured products.
     * @param model the Spring Model object
     * @return the home view
     */
    @GetMapping("/")
    public String home(Model model) {
        // Get recent products for home page display
        model.addAttribute("recentProducts", 
            productService.getAllProducts().stream().limit(6).toList());
        return "home";
    }
    
    /**
     * Displays about page.
     * @return the about view
     */
    @GetMapping("/about")
    public String about() {
        return "about";
    }
    
    /**
     * Displays contact page.
     * @return the contact view
     */
    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }
}