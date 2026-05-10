package com.nexusstore.nexusstore.services;

import com.nexusstore.nexusstore.models.ProductModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * In-memory implementation of {@link ProductService}.
 * Stores products in a list for Milestone 3 (no database yet).
 * Will be refactored to use Spring Data JDBC in Milestone 4.
 *
 * Registered as a Spring Bean via {@code @Service} for IoC/DI.
 */
@Service
public class ProductServiceImpl implements ProductService {

    /** In-memory product store (replaces database for M3). */
    private final List<ProductModel> products = new ArrayList<>();

    /** Auto-incrementing ID counter. */
    private final AtomicInteger idCounter = new AtomicInteger(1);

    /**
     * Initializes the service with sample products for demonstration.
     */
    public ProductServiceImpl() {
        products.add(new ProductModel(idCounter.getAndIncrement(),
                "Samsung 65\" QLED TV",
                "4K Ultra HD Smart TV with Quantum HDR and Alexa Built-in.",
                899.99, 15, "Electronics",
                "https://placehold.co/300x200/1a1a2e/ffffff?text=Samsung+TV"));

        products.add(new ProductModel(idCounter.getAndIncrement(),
                "Apple iPhone 15",
                "6.1-inch Super Retina XDR display, 48MP camera, USB-C connector.",
                799.99, 30, "Electronics",
                "https://placehold.co/300x200/1a1a2e/ffffff?text=iPhone+15"));

        products.add(new ProductModel(idCounter.getAndIncrement(),
                "Nike Air Max 270",
                "Lightweight running shoe with Max Air unit and breathable mesh upper.",
                150.00, 50, "Apparel",
                "https://placehold.co/300x200/2d6a4f/ffffff?text=Nike+Air+Max"));

        products.add(new ProductModel(idCounter.getAndIncrement(),
                "Levi's 501 Original Jeans",
                "Classic straight fit jeans in 100% cotton denim. Iconic since 1873.",
                69.99, 75, "Apparel",
                "https://placehold.co/300x200/2d6a4f/ffffff?text=Levis+501"));

        products.add(new ProductModel(idCounter.getAndIncrement(),
                "Sony WH-1000XM5 Headphones",
                "Industry-leading noise canceling wireless headphones with 30-hour battery.",
                349.99, 20, "Accessories",
                "https://placehold.co/300x200/6b4c93/ffffff?text=Sony+XM5"));

        products.add(new ProductModel(idCounter.getAndIncrement(),
                "Anker USB-C Charging Cable 6ft",
                "Braided nylon USB-C to USB-C cable, supports 100W fast charging.",
                19.99, 200, "Accessories",
                "https://placehold.co/300x200/6b4c93/ffffff?text=Anker+Cable"));
    }

    /**
     * Returns all products in the store.
     *
     * @return list of all products
     */
    @Override
    public List<ProductModel> getAllProducts() {
        return new ArrayList<>(products);
    }

    /**
     * Returns products filtered by category (case-insensitive).
     *
     * @param category the category to filter by
     * @return filtered list of products
     */
    @Override
    public List<ProductModel> getProductsByCategory(String category) {
        return products.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    /**
     * Finds a product by its unique ID.
     *
     * @param id the product ID
     * @return the matching product or {@code null} if not found
     */
    @Override
    public ProductModel getProductById(int id) {
        return products.stream()
                .filter(p -> p.getProductId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Adds a new product, assigning it the next available ID.
     *
     * @param product the product to add
     */
    @Override
    public void addProduct(ProductModel product) {
        product.setProductId(idCounter.getAndIncrement());
        products.add(product);
    }

    /**
     * Updates an existing product by replacing the matching entry.
     *
     * @param product the product with updated values (must have a valid ID)
     */
    @Override
    public void updateProduct(ProductModel product) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getProductId() == product.getProductId()) {
                products.set(i, product);
                return;
            }
        }
    }

    /**
     * Removes a product from the store by ID.
     *
     * @param id the ID of the product to delete
     */
    @Override
    public void deleteProduct(int id) {
        products.removeIf(p -> p.getProductId() == id);
    }
}
