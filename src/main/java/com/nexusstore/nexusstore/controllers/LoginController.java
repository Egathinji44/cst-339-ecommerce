package com.nexusstore.nexusstore.controllers;

import com.nexusstore.nexusstore.models.UserModel;
import com.nexusstore.nexusstore.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Spring MVC Controller handling user login and logout.
 * Uses {@link UserService} via dependency injection (IoC/DI).
 */
@Controller
public class LoginController {

    /** Injected UserService Spring Bean for authentication logic. */
    private final UserService userService;

    /**
     * Constructor-based dependency injection of {@link UserService}.
     *
     * @param userService the user service bean
     */
    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Displays the login page.
     *
     * @return the login template name
     */
    @GetMapping("/login")
    public String showLoginPage() {
        return "auth/login";
    }

    /**
     * Processes the login form submission.
     * Validates credentials via {@link UserService}, stores user in session on success.
     *
     * @param username the entered username
     * @param password the entered password
     * @param session  the HTTP session for storing the logged-in user
     * @param model    the Spring MVC model for passing error messages to the view
     * @return redirect to home on success, or re-render login on failure
     */
    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {
        UserModel user = userService.login(username, password);
        if (user != null) {
            session.setAttribute("loggedInUser", user);
            return "redirect:/";
        }
        model.addAttribute("error", "Invalid username or password. Please try again.");
        return "auth/login";
    }

    /**
     * Logs out the current user by invalidating the session.
     *
     * @param session the current HTTP session
     * @return redirect to login page
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
