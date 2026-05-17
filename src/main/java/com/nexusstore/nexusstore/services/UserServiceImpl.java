package com.nexusstore.nexusstore.services;

import com.nexusstore.nexusstore.models.UserModel;
import com.nexusstore.nexusstore.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Spring Data JDBC implementation of {@link UserService}.
 *
 * <p>Refactored for Milestone 4: replaces the in-memory {@code ArrayList}
 * with a {@link UserRepository} backed by MySQL via Spring Data JDBC.
 * Seed data (default admin account) is now handled by {@code data.sql}
 * rather than a constructor initializer.
 *
 * <p>Registered as a Spring Bean via {@code @Service} for IoC/DI.
 */
@Service
public class UserServiceImpl implements UserService {

    /** Spring Data JDBC repository injected via constructor DI. */
    private final UserRepository userRepository;

    /**
     * Constructor-based dependency injection of {@link UserRepository}.
     *
     * @param userRepository the Spring Data JDBC user repository
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Registers a new user if the username is not already taken.
     * The new user is always assigned the role {@code USER}.
     * The default admin account is seeded via {@code data.sql}.
     *
     * @param user the user to register (id should be null for new records)
     * @return {@code true} if registration succeeded; {@code false} if username is taken
     */
    @Override
    public boolean registerUser(UserModel user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return false;
        }
        user.setId(null);       // ensure INSERT
        user.setRole("USER");   // enforce non-admin registration
        userRepository.save(user);
        return true;
    }

    /**
     * Authenticates a user by matching username and password against the database.
     *
     * @param username entered username
     * @param password entered password (plain-text; hashing deferred to Milestone 6 Spring Security)
     * @return the matching {@link UserModel}, or {@code null} if credentials are invalid
     */
    @Override
    public UserModel login(String username, String password) {
        Optional<UserModel> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            return userOpt.get();
        }
        return null;
    }
}
