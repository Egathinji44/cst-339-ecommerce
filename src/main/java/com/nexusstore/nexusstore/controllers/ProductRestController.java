package com.nexusstore.nexusstore.controllers;

import com.nexusstore.nexusstore.models.ProductModel;
import com.nexusstore.nexusstore.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API controller exposing product data over HTTP.
 *
 * <p>Milestone 7: Adds two secured REST endpoints:
 * <ul>
 *   <li>GET /api/products       – returns all products as JSON</li>
 *   <li>GET /api/products/{id}  – returns one product by ID as JSON</li>
 * </ul>
 *
 * <p>Both endpoints are secured via HTTP Basic Authentication backed by the
 * MongoDB {@code users} collection (configured in
 * {@link com.nexusstore.nexusstore.config.SecurityConfig}).
 * Any valid user (USER or ADMIN role) may call these endpoints.
 */
@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    private final ProductService productService;

    /**
     * Constructor-based DI of {@link ProductService}.
     *
     * @param productService the product service bean
     */
    @Autowired
    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * REST API 1: Returns all products in the store as a JSON array.
     *
     * <p>Requires: HTTP Basic Authentication (any authenticated user).
     *
     * @return 200 OK with a JSON array of all {@link ProductModel} objects,
     *         or 204 No Content if no products exist
     */
    @GetMapping
    public ResponseEntity<List<ProductModel>> getAllProducts() {
        List<ProductModel> products = productService.getAllProducts();
        if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(products);
    }

    /**
     * REST API 2: Returns a single product by its MongoDB ObjectId.
     *
     * <p>Requires: HTTP Basic Authentication (any authenticated user).
     *
     * @param id the MongoDB ObjectId string of the desired product
     * @return 200 OK with the matching {@link ProductModel} as JSON,
     *         or 404 Not Found if no product with that ID exists
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductModel> getProductById(@PathVariable String id) {
        ProductModel product = productService.getProductById(id);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(product);
    }
}
