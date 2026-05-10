package com.nexusstore.nexusstore.services;

import com.nexusstore.nexusstore.models.UserModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * In-memory implementation of {@link UserService}.
 * Stores users in a list for Milestone 3 (no database yet).
 * Will be refactored to use Spring Data JDBC in Milestone 4.
 *
 * Registered as a Spring Bean via {@code @Service} for IoC/DI.
 */
@Service
public class UserServiceImpl implements UserService {

    /** In-memory user store (replaces database for M3). */
    private final List<UserModel> users = new ArrayList<>();

    /** Auto-incrementing ID counter. */
    private final AtomicInteger idCounter = new AtomicInteger(1);

    /**
     * Initializes the service with a default admin user for testing.
     */
    public UserServiceImpl() {
        // Seed a default admin account for testing
        UserModel admin = new UserModel(
                idCounter.getAndIncrement(),
                "Admin", "User",
                "admin@nexusstore.com",
                "admin", "admin123", "ADMIN"
        );
        users.add(admin);
    }

    /**
     * Registers a new user if the username is not already taken.
     *
     * @param user the user to register
     * @return {@code true} if successful, {@code false} if username is taken
     */
    @Override
    public boolean registerUser(UserModel user) {
        // Check for duplicate username
        boolean exists = users.stream()
                .anyMatch(u -> u.getUsername().equalsIgnoreCase(user.getUsername()));
        if (exists) {
            return false;
        }
        user.setId(idCounter.getAndIncrement());
        user.setRole("USER");
        users.add(user);
        return true;
    }

    /**
     * Authenticates a user by matching username and password.
     *
     * @param username entered username
     * @param password entered password
     * @return the matching {@link UserModel} or {@code null} if credentials are invalid
     */
    @Override
    public UserModel login(String username, String password) {
        return users.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username)
                          && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }
}
