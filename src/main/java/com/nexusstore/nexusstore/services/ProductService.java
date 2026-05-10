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
     * @param id the product ID
     * @return the matching {@link ProductModel}, or {@code null} if not found
     */
    ProductModel getProductById(int id);

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
     * @param id the ID of the product to delete
     */
    void deleteProduct(int id);
}
