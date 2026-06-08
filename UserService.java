// src/main/java/com/nexusstore/service/UserService.java
package com.nexusstore.service;

import com.nexusstore.dto.UserRegistrationDto;
import com.nexusstore.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Service interface for user management operations.
 * 
 * @author NexusStore Team
 * @version 1.0
 */
public interface UserService extends UserDetailsService {
    
    /**
     * Register a new user.
     * @param registrationDto the user registration data
     * @return the registered user
     * @throws RuntimeException if username or email already exists
     */
    User registerUser(UserRegistrationDto registrationDto);
    
    /**
     * Find user by username.
     * @param username the username to search for
     * @return the found user
     * @throws RuntimeException if user not found
     */
    User findByUsername(String username);
    
    /**
     * Find user by email.
     * @param email the email to search for
     * @return the found user
     * @throws RuntimeException if user not found
     */
    User findByEmail(String email);
    
    /**
     * Check if username is available.
     * @param username the username to check
     * @return true if available
     */
    boolean isUsernameAvailable(String username);
    
    /**
     * Check if email is available.
     * @param email the email to check
     * @return true if available
     */
    boolean isEmailAvailable(String email);
}