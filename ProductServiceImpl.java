// src/main/java/com/nexusstore/service/ProductServiceImpl.java
package com.nexusstore.service;

import com.nexusstore.dto.ProductDto;
import com.nexusstore.model.Product;
import com.nexusstore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of ProductService interface.
 * Contains all business logic for product management.
 * 
 * @author NexusStore Team
 * @version 1.0
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    /**
     * Creates a new product after validating SKU uniqueness.
     * 
     * @param productDto the product data
     * @return the created product
     * @throws RuntimeException if SKU already exists
     */
    @Override
    public Product createProduct(ProductDto productDto) {
        // Validate SKU uniqueness
        if (productRepository.findBySku(productDto.getSku()).isPresent()) {
            throw new RuntimeException("Product with SKU " + productDto.getSku() + " already exists");
        }
        
        // Convert DTO to Entity
        Product product = new Product(
            productDto.getSku(),
            productDto.getName(),
            productDto.getDescription(),
            productDto.getPrice(),
            productDto.getQuantity(),
            productDto.getCategory()
        );
        
        product.setImageUrl(productDto.getImageUrl());
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        
        return productRepository.save(product);
    }
    
    /**
     * Updates an existing product.
     * 
     * @param id the product ID
     * @param productDto the updated product data
     * @return the updated product
     * @throws RuntimeException if product not found or SKU conflict
     */
    @Override
    public Product updateProduct(Long id, ProductDto productDto) {
        Product existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        
        // Check SKU uniqueness if changed
        if (!existingProduct.getSku().equals(productDto.getSku()) &&
            productRepository.findBySku(productDto.getSku()).isPresent()) {
            throw new RuntimeException("Product with SKU " + productDto.getSku() + " already exists");
        }
        
        // Update fields
        existingProduct.setSku(productDto.getSku());
        existingProduct.setName(productDto.getName());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setQuantity(productDto.getQuantity());
        existingProduct.setCategory(productDto.getCategory());
        existingProduct.setImageUrl(productDto.getImageUrl());
        existingProduct.setUpdatedAt(LocalDateTime.now());
        
        return productRepository.save(existingProduct);
    }
    
    /**
     * Soft deletes a product (sets active to false).
     * 
     * @param id the product ID
     * @throws RuntimeException if product not found
     */
    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.updateActiveStatus(id, false);
    }
    
    /**
     * Finds a product by ID.
     * 
     * @param id the product ID
     * @return Optional containing the product
     */
    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }
    
    /**
     * Finds a product by SKU.
     * 
     * @param sku the SKU
     * @return Optional containing the product
     */
    @Override
    public Optional<Product> findBySku(String sku) {
        return productRepository.findBySku(sku);
    }
    
    /**
     * Gets all active products.
     * 
     * @return list of all active products
     */
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAllActive();
    }
    
    /**
     * Gets products by category.
     * 
     * @param category the category
     * @return list of products in the category
     */
    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }
    
    /**
     * Searches products by keyword.
     * 
     * @param keyword the search keyword
     * @return list of matching products
     */
    @Override
    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllProducts();
        }
        return productRepository.searchProducts(keyword.trim());
    }
    
    /**
     * Checks if a SKU is available.
     * 
     * @param sku the SKU to check
     * @return true if available
     */
    @Override
    public boolean isSkuAvailable(String sku) {
        return productRepository.findBySku(sku).isEmpty();
    }
}