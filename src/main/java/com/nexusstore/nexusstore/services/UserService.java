package com.nexusstore.nexusstore.services;

import com.nexusstore.nexusstore.models.UserModel;

/**
 * Service interface for user management operations.
 * Authentication is handled by Spring Security via CustomUserDetailsService.
 */
public interface UserService {

    /**
     * Registers a new user account with a BCrypt-encoded password.
     *
     * @param user the UserModel to register
     * @return true if registration succeeded; false if the username is already taken
     */
    boolean registerUser(UserModel user);
}
