package com.nexusstore.nexusstore.config;

import com.nexusstore.nexusstore.models.ProductModel;
import com.nexusstore.nexusstore.models.UserModel;
import com.nexusstore.nexusstore.repositories.ProductRepository;
import com.nexusstore.nexusstore.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Seeds MongoDB with default admin and sample products on startup.
 * Seeding is idempotent — only runs when collections are empty.
 * Also migrates any existing plain-text passwords to BCrypt (M6).
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataSeeder(UserRepository userRepository,
                      ProductRepository productRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedUsers();
        seedProducts();
    }

    private void seedUsers() {
        if (userRepository.count() == 0) {
            userRepository.save(new UserModel(null, "Admin", "User",
                    "admin@nexusstore.com", "admin",
                    passwordEncoder.encode("admin123"), "ADMIN"));
            System.out.println("[DataSeeder] Admin account created.");
        } else {
            // M6 migration: upgrade any stored plain-text passwords to BCrypt
            userRepository.findAll().forEach(user -> {
                if (!user.getPassword().startsWith("$2a$")) {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    userRepository.save(user);
                    System.out.println("[DataSeeder] Migrated password for: " + user.getUsername());
                }
            });
        }
    }

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
