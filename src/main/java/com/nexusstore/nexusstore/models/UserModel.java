package com.nexusstore.nexusstore.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Model representing a user account in NexusStore.
 * Holds registration and login data with validation constraints.
 */
public class UserModel {

    private int id;

    /** User's first name. Required. */
    @NotBlank(message = "First name is required")
    private String firstName;

    /** User's last name. Required. */
    @NotBlank(message = "Last name is required")
    private String lastName;

    /** User's email address. Must be a valid email format. */
    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email address")
    private String email;

    /** Username for login. Must be 3–20 characters. */
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    /** Password. Minimum 6 characters. */
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    /** Role: USER or ADMIN */
    private String role = "USER";

    // ── Constructors ──────────────────────────────────────────────────────────

    /** Default no-arg constructor required by Spring MVC model binding. */
    public UserModel() {}

    /**
     * Full constructor.
     *
     * @param id        unique user ID
     * @param firstName user's first name
     * @param lastName  user's last name
     * @param email     user's email
     * @param username  login username
     * @param password  login password
     * @param role      user role (USER or ADMIN)
     */
    public UserModel(int id, String firstName, String lastName,
                     String email, String username, String password, String role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    /** @return the user's unique ID */
    public int getId() { return id; }

    /** @param id the user's unique ID */
    public void setId(int id) { this.id = id; }

    /** @return the user's first name */
    public String getFirstName() { return firstName; }

    /** @param firstName the user's first name */
    public void setFirstName(String firstName) { this.firstName = firstName; }

    /** @return the user's last name */
    public String getLastName() { return lastName; }

    /** @param lastName the user's last name */
    public void setLastName(String lastName) { this.lastName = lastName; }

    /** @return the user's email */
    public String getEmail() { return email; }

    /** @param email the user's email */
    public void setEmail(String email) { this.email = email; }

    /** @return the username */
    public String getUsername() { return username; }

    /** @param username the username */
    public void setUsername(String username) { this.username = username; }

    /** @return the password */
    public String getPassword() { return password; }

    /** @param password the password */
    public void setPassword(String password) { this.password = password; }

    /** @return the user role */
    public String getRole() { return role; }

    /** @param role the user role */
    public void setRole(String role) { this.role = role; }
}
