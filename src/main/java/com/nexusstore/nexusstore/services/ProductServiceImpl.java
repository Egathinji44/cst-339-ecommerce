package com.nexusstore.nexusstore.services;

import com.nexusstore.nexusstore.models.ProductModel;
import com.nexusstore.nexusstore.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Spring Data MongoDB implementation of {@link ProductService}.
 *
 * <p>Milestone 5: replaces the Spring Data JDBC / MySQL persistence layer with
 * {@link ProductRepository} backed by MongoDB via Spring Data MongoDB.
 * All CRUD operations are delegated to the repository's {@code MongoRepository}
 * methods, keeping business logic cleanly separated from data access.
 *
 * <p>Registered as a Spring Bean via {@code @Service} for IoC/DI.
 */
@Service
public class ProductServiceImpl implements ProductService {

    /** Spring Data MongoDB repository injected via constructor DI. */
    private final ProductRepository productRepository;

    /**
     * Constructor-based dependency injection of {@link ProductRepository}.
     *
     * @param productRepository the Spring Data MongoDB product repository
     */
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Returns all products stored in the MongoDB collection.
     *
     * @return list of all products
     */
    @Override
    public List<ProductModel> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Returns products filtered by category.
     *
     * @param category the category to filter by
     * @return filtered list of products
     */
    @Override
    public List<ProductModel> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    /**
     * Finds a product by its MongoDB ObjectId string.
     *
     * @param id the product's MongoDB document ID
     * @return the matching {@link ProductModel}, or {@code null} if not found
     */
    @Override
    public ProductModel getProductById(String id) {
        return productRepository.findById(id).orElse(null);
    }

    /**
     * Persists a new product to the MongoDB collection.
     * Setting {@code productId} to {@code null} ensures MongoDB assigns a fresh ObjectId.
     *
     * @param product the product to save (productId should be null)
     */
    @Override
    public void addProduct(ProductModel product) {
        product.setProductId(null);
        productRepository.save(product);
    }

    /**
     * Updates an existing product in the MongoDB collection.
     *
     * @param product the product with updated values (must have a valid ID)
     */
    @Override
    public void updateProduct(ProductModel product) {
        productRepository.save(product);
    }

    /**
     * Deletes a product by its MongoDB ObjectId string.
     *
     * @param id the ID of the product to delete
     */
    @Override
    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    /**
     * Searches for products whose name contains the given keyword (case-insensitive).
     *
     * @param keyword the search term
     * @return list of matching products
     */
    @Override
    public List<ProductModel> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }
}