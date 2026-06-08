// src/main/java/com/nexusstore/service/ProductService.java
package com.nexusstore.service;

import com.nexusstore.dto.ProductDto;
import com.nexusstore.model.Product;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for product management operations.
 * 
 * @author NexusStore Team
 * @version 1.0
 */
public interface ProductService {
    
    /**
     * Create a new product.
     * @param productDto the product data
     * @return the created product
     * @throws RuntimeException if SKU already exists
     */
    Product createProduct(ProductDto productDto);
    
    /**
     * Update an existing product.
     * @param id the product ID
     * @param productDto the updated product data
     * @return the updated product
     * @throws RuntimeException if product not found
     */
    Product updateProduct(Long id, ProductDto productDto);
    
    /**
     * Delete a product (soft delete).
     * @param id the product ID
     * @throws RuntimeException if product not found
     */
    void deleteProduct(Long id);
    
    /**
     * Find product by ID.
     * @param id the product ID
     * @return Optional containing the product
     */
    Optional<Product> findById(Long id);
    
    /**
     * Find product by SKU.
     * @param sku the SKU
     * @return Optional containing the product
     */
    Optional<Product> findBySku(String sku);
    
    /**
     * Get all active products.
     * @return list of all active products
     */
    List<Product> getAllProducts();
    
    /**
     * Get products by category.
     * @param category the category
     * @return list of products in the category
     */
    List<Product> getProductsByCategory(String category);
    
    /**
     * Search products by keyword.
     * @param keyword the search keyword
     * @return list of matching products
     */
    List<Product> searchProducts(String keyword);
    
    /**
     * Check if SKU is available.
     * @param sku the SKU to check
     * @return true if available
     */
    boolean isSkuAvailable(String sku);
}