package com.nexusstore.nexusstore.controllers;

import com.nexusstore.nexusstore.models.UserModel;
import com.nexusstore.nexusstore.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Spring MVC Controller handling user registration.
 * Uses {@link UserService} via dependency injection (IoC/DI).
 */
@Controller
public class RegisterController {

    /** Injected UserService Spring Bean for user registration logic. */
    private final UserService userService;

    /**
     * Constructor-based dependency injection of {@link UserService}.
     *
     * @param userService the user service bean
     */
    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Displays the registration form with an empty {@link UserModel}.
     *
     * @param model the Spring MVC model
     * @return the register template name
     */
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new UserModel());
        return "auth/register";
    }

    /**
     * Processes the registration form submission.
     * Validates form fields, checks for duplicate usernames,
     * and registers the user via {@link UserService}.
     *
     * @param user          the {@link UserModel} bound from the form
     * @param bindingResult holds validation errors from {@code @Valid}
     * @param model         the Spring MVC model for passing messages to the view
     * @return redirect to login on success, or re-render register form on failure
     */
    @PostMapping("/register")
    public String processRegister(@Valid @ModelAttribute("user") UserModel user,
                                  BindingResult bindingResult,
                                  Model model) {
        // Return to form if validation errors exist
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        boolean success = userService.registerUser(user);
        if (!success) {
            model.addAttribute("error", "Username already taken. Please choose a different one.");
            return "auth/register";
        }

        return "redirect:/login?registered=true";
    }
}
