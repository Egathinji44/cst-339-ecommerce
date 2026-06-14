package com.nexusstore.nexusstore.services;

import com.nexusstore.nexusstore.models.ProductModel;
import java.util.List;

/**
 * Service interface defining CRUD business operations for product management.
 * Implementations are registered as Spring Beans via {@code @Service}.
 */
public interface ProductService {

    /**
     * Returns all products in the store.
     *
     * @return list of all {@link ProductModel} objects
     */
    List<ProductModel> getAllProducts();

    /**
     * Returns all products matching the given category.
     *
     * @param category the category to filter by
     * @return filtered list of {@link ProductModel} objects
     */
    List<ProductModel> getProductsByCategory(String category);

    /**
     * Finds a single product by its ID.
     *
     * @param id the product ID (MongoDB ObjectId string)
     * @return the matching {@link ProductModel}, or {@code null} if not found
     */
    ProductModel getProductById(String id);

    /**
     * Adds a new product to the store.
     *
     * @param product the {@link ProductModel} to add
     */
    void addProduct(ProductModel product);

    /**
     * Updates an existing product's details.
     *
     * @param product the {@link ProductModel} with updated values
     */
    void updateProduct(ProductModel product);

    /**
     * Deletes a product by its ID.
     *
     * @param id the ID of the product to delete (MongoDB ObjectId string)
     */
    void deleteProduct(String id);

    /**
     * Searches for products whose name contains the given keyword (case-insensitive).
     *
     * @param keyword the search term
     * @return list of matching products
     */
    List<ProductModel> searchProducts(String keyword);

    /**
     * Decreases the available stock quantity for a product, typically called
     * when an order is placed (Milestone 8: Shopping Cart &amp; Ordering module).
     * Stock will not be reduced below zero.
     *
     * @param id       the ID of the product whose stock should be decreased
     * @param quantity the number of units to subtract from current stock
     */
    void decreaseStock(String id, int quantity);
}