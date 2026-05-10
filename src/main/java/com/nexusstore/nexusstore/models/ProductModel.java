package com.nexusstore.nexusstore.models;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

/**
 * Model representing a product in NexusStore.
 * Covers all product attributes defined in the project specification.
 */
public class ProductModel {

    private int productId;

    /** Product name. Required. */
    @NotBlank(message = "Product name is required")
    private String name;

    /** Detailed description of the product. Required. */
    @NotBlank(message = "Description is required")
    private String description;

    /** Product price. Must be greater than 0. */
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private Double price;

    /** Number of units in stock. Cannot be negative. */
    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    private Integer stockQuantity;

    /** Product category: Apparel, Accessories, or Electronics. Required. */
    @NotBlank(message = "Category is required")
    private String category;

    /** URL to the product image. Optional. */
    private String imageUrl;

    /** Date the product was added. Auto-set on creation. */
    private LocalDate dateAdded;

    // ── Constructors ──────────────────────────────────────────────────────────

    /** Default no-arg constructor required by Spring MVC model binding. */
    public ProductModel() {
        this.dateAdded = LocalDate.now();
    }

    /**
     * Full constructor.
     *
     * @param productId     unique product ID
     * @param name          product name
     * @param description   product description
     * @param price         product price
     * @param stockQuantity units in stock
     * @param category      product category
     * @param imageUrl      URL to product image
     */
    public ProductModel(int productId, String name, String description,
                        Double price, Integer stockQuantity,
                        String category, String imageUrl) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
        this.imageUrl = imageUrl;
        this.dateAdded = LocalDate.now();
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    /** @return the product's unique ID */
    public int getProductId() { return productId; }

    /** @param productId the product's unique ID */
    public void setProductId(int productId) { this.productId = productId; }

    /** @return the product name */
    public String getName() { return name; }

    /** @param name the product name */
    public void setName(String name) { this.name = name; }

    /** @return the product description */
    public String getDescription() { return description; }

    /** @param description the product description */
    public void setDescription(String description) { this.description = description; }

    /** @return the product price */
    public Double getPrice() { return price; }

    /** @param price the product price */
    public void setPrice(Double price) { this.price = price; }

    /** @return the stock quantity */
    public Integer getStockQuantity() { return stockQuantity; }

    /** @param stockQuantity the stock quantity */
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }

    /** @return the product category */
    public String getCategory() { return category; }

    /** @param category the product category */
    public void setCategory(String category) { this.category = category; }

    /** @return the image URL */
    public String getImageUrl() { return imageUrl; }

    /** @param imageUrl the image URL */
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    /** @return the date the product was added */
    public LocalDate getDateAdded() { return dateAdded; }

    /** @param dateAdded the date added */
    public void setDateAdded(LocalDate dateAdded) { this.dateAdded = dateAdded; }
}
