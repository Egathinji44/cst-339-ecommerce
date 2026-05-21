package com.nexusstore.nexusstore.config;

import com.nexusstore.nexusstore.models.ProductModel;
import com.nexusstore.nexusstore.models.UserModel;
import com.nexusstore.nexusstore.repositories.ProductRepository;
import com.nexusstore.nexusstore.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Seed component that populates the MongoDB database with default data on startup.
 * Replaces the MySQL schema.sql and data.sql files from Milestone 4.
 * Seeding is idempotent — only runs when collections are empty.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    /** Repository for user persistence operations. */
    private final UserRepository userRepository;

    /** Repository for product persistence operations. */
    private final ProductRepository productRepository;

    /**
     * Constructor-based dependency injection of repositories.
     *
     * @param userRepository    the Spring Data MongoDB user repository
     * @param productRepository the Spring Data MongoDB product repository
     */
    @Autowired
    public DataSeeder(UserRepository userRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    /**
     * Seeds the database on application startup if collections are empty.
     *
     * @param args command-line arguments (not used)
     */
    @Override
    public void run(String... args) {
        seedUsers();
        seedProducts();
    }

    /**
     * Inserts the default admin account if no users exist yet.
     * Username: admin / Password: admin123
     */
    private void seedUsers() {
        if (userRepository.count() == 0) {
            userRepository.save(new UserModel(null, "Admin", "User",
                    "admin@nexusstore.com", "admin", "admin123", "ADMIN"));
            System.out.println("[DataSeeder] Admin account created.");
        }
    }

    /**
     * Inserts six sample products if no products exist yet.
     */
    private void seedProducts() {
        if (productRepository.count() == 0) {
            productRepository.save(new ProductModel(null, "Samsung 65\" QLED TV",
                    "4K Ultra HD Smart TV with Quantum HDR and Alexa Built-in.",
                    899.99, 15, "Electronics",
                    "https://placehold.co/300x200/1a1a2e/ffffff?text=Samsung+TV"));
            productRepository.save(new ProductModel(null, "Apple iPhone 15",
                    "6.1-inch Super Retina XDR display, 48MP camera, USB-C connector.",
                    799.99, 30, "Electronics",
                    "https://placehold.co/300x200/1a1a2e/ffffff?text=iPhone+15"));
            productRepository.save(new ProductModel(null, "Nike Air Max 270",
                    "Lightweight running shoe with Max Air unit and breathable mesh upper.",
                    150.00, 50, "Apparel",
                    "https://placehold.co/300x200/2d6a4f/ffffff?text=Nike+Air+Max"));
            productRepository.save(new ProductModel(null, "Levi's 501 Original Jeans",
                    "Classic straight fit jeans in 100% cotton denim. Iconic since 1873.",
                    69.99, 75, "Apparel",
                    "https://placehold.co/300x200/2d6a4f/ffffff?text=Levis+501"));
            productRepository.save(new ProductModel(null, "Sony WH-1000XM5 Headphones",
                    "Industry-leading noise canceling wireless headphones with 30-hour battery.",
                    349.99, 20, "Accessories",
                    "https://placehold.co/300x200/6b4c93/ffffff?text=Sony+XM5"));
            productRepository.save(new ProductModel(null, "Anker USB-C Charging Cable 6ft",
                    "Braided nylon USB-C to USB-C cable, supports 100W fast charging.",
                    19.99, 200, "Accessories",
                    "https://placehold.co/300x200/6b4c93/ffffff?text=Anker+Cable"));
            System.out.println("[DataSeeder] 6 sample products created.");
        }
    }
}