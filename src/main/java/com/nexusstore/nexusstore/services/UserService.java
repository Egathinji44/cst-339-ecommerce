package com.nexusstore.nexusstore.services;

import com.nexusstore.nexusstore.models.UserModel;

/**
 * Service interface defining business operations for user management.
 * Implementations are registered as Spring Beans via {@code @Service}.
 */
public interface UserService {

    /**
     * Registers a new user account.
     *
     * @param user the {@link UserModel} to register
     * @return {@code true} if registration succeeded; {@code false} if username already exists
     */
    boolean registerUser(UserModel user);

    /**
     * Validates login credentials.
     *
     * @param username the entered username
     * @param password the entered password
     * @return the matching {@link UserModel} if credentials are valid, or {@code null} if not
     */
    UserModel login(String username, String password);
}
