package com.nexusstore.nexusstore.services;

import com.nexusstore.nexusstore.models.ProductModel;
import com.nexusstore.nexusstore.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring Data JDBC implementation of {@link ProductService}.
 *
 * <p>Refactored for Milestone 4: replaces the in-memory {@code ArrayList}
 * with a {@link ProductRepository} backed by MySQL via Spring Data JDBC.
 * All CRUD operations are delegated to the repository's {@code CrudRepository}
 * methods, keeping business logic cleanly separated from data access.
 *
 * <p>Registered as a Spring Bean via {@code @Service} for IoC/DI.
 */
@Service
public class ProductServiceImpl implements ProductService {

    /** Spring Data JDBC repository injected via constructor DI. */
    private final ProductRepository productRepository;

    /**
     * Constructor-based dependency injection of {@link ProductRepository}.
     *
     * @param productRepository the Spring Data JDBC product repository
     */
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Returns all products stored in the database.
     *
     * @return list of all products
     */
    @Override
    public List<ProductModel> getAllProducts() {
        List<ProductModel> products = new ArrayList<>();
        productRepository.findAll().forEach(products::add);
        return products;
    }

    /**
     * Returns products filtered by category.
     * Delegates to the derived query {@code findByCategory} in the repository.
     *
     * @param category the category to filter by
     * @return filtered list of products
     */
    @Override
    public List<ProductModel> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    /**
     * Finds a product by its unique database ID.
     *
     * @param id the product's primary key
     * @return the matching {@link ProductModel}, or {@code null} if not found
     */
    @Override
    public ProductModel getProductById(int id) {
        return productRepository.findById(id).orElse(null);
    }

    /**
     * Persists a new product to the database.
     * Spring Data JDBC detects a new record when {@code productId} is {@code null}
     * and issues an INSERT; MySQL AUTO_INCREMENT assigns the ID.
     *
     * @param product the product to save (productId should be null)
     */
    @Override
    public void addProduct(ProductModel product) {
        product.setProductId(null); // ensure INSERT, not UPDATE
        productRepository.save(product);
    }

    /**
     * Updates an existing product in the database.
     * Spring Data JDBC issues an UPDATE when {@code productId} is set.
     *
     * @param product the product with updated values (must have a valid ID)
     */
    @Override
    public void updateProduct(ProductModel product) {
        productRepository.save(product);
    }

    /**
     * Deletes a product by its database ID.
     *
     * @param id the ID of the product to delete
     */
    @Override
    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }
}
