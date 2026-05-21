package com.nexusstore.nexusstore.services;

import com.nexusstore.nexusstore.models.UserModel;
import com.nexusstore.nexusstore.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Spring Data MongoDB implementation of {@link UserService}.
 *
 * <p>Registered as a Spring Bean via {@code @Service} for IoC/DI.
 */
@Service
public class UserServiceImpl implements UserService {

    /** Spring Data MongoDB repository injected via constructor DI. */
    private final UserRepository userRepository;

    /**
     * Constructor-based dependency injection of {@link UserRepository}.
     *
     * @param userRepository the Spring Data MongoDB user repository
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Registers a new user if the username is not already taken.
     *
     * @param user the user to register
     * @return {@code true} if registration succeeded; {@code false} if username is taken
     */
    @Override
    public boolean registerUser(UserModel user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return false;
        }
        user.setId(null);
        user.setRole("USER");
        userRepository.save(user);
        return true;
    }

    /**
     * Authenticates a user by matching username and password against the database.
     *
     * @param username entered username
     * @param password entered password
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