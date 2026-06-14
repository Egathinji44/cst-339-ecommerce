package com.nexusstore.nexusstore.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Embedded value object representing a shipping address.
 *
 * <p>Used both as the form-backing object on the checkout page and as an
 * embedded field within {@link OrderModel} so each order retains a permanent
 * record of where it was shipped.</p>
 *
 * <p>Milestone 8: Added as part of the Shopping Cart &amp; Ordering module.</p>
 */
public class ShippingAddress {

    /** Recipient's full name. Required. */
    @NotBlank(message = "Full name is required")
    private String fullName;

    /** Street address, including house/apartment number. Required. */
    @NotBlank(message = "Street address is required")
    private String street;

    /** City name. Required. */
    @NotBlank(message = "City is required")
    private String city;

    /** State or province (2-letter code, e.g. AZ). Required. */
    @NotBlank(message = "State is required")
    private String state;

    /** Postal / ZIP code. Required, 5 digits. */
    @NotBlank(message = "ZIP code is required")
    @Pattern(regexp = "\\d{5}", message = "ZIP code must be 5 digits")
    private String zipCode;

    /**
     * Default no-arg constructor required by Spring MVC model binding
     * and Spring Data MongoDB mapping.
     */
    public ShippingAddress() {
    }

    /**
     * Full constructor for programmatic creation.
     *
     * @param fullName recipient's full name
     * @param street   street address
     * @param city     city
     * @param state    state or province
     * @param zipCode  postal / ZIP code
     */
    public ShippingAddress(String fullName, String street, String city, String state, String zipCode) {
        this.fullName = fullName;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    /** @return the recipient's full name */
    public String getFullName() { return fullName; }

    /** @param fullName the recipient's full name */
    public void setFullName(String fullName) { this.fullName = fullName; }

    /** @return the street address */
    public String getStreet() { return street; }

    /** @param street the street address */
    public void setStreet(String street) { this.street = street; }

    /** @return the city */
    public String getCity() { return city; }

    /** @param city the city */
    public void setCity(String city) { this.city = city; }

    /** @return the state or province */
    public String getState() { return state; }

    /** @param state the state or province */
    public void setState(String state) { this.state = state; }

    /** @return the postal / ZIP code */
    public String getZipCode() { return zipCode; }

    /** @param zipCode the postal / ZIP code */
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }
}
