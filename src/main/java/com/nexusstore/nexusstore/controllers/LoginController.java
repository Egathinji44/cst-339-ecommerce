package com.nexusstore.nexusstore.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Renders the login page. Spring Security handles the POST /login and GET /logout
 * internally; this controller only serves the view for the login form.
 */
@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "auth/login";
    }
}
