package com.nexusstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for NexusStore.
 * Spring Boot application entry point.
 * 
 * @author NexusStore Team
 * @version 1.0
 */
@SpringBootApplication
public class NexusstoreApplication {
    
    /**
     * Main method to run the Spring Boot application.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(NexusstoreApplication.class, args);
        System.out.println("========================================");
        System.out.println("NexusStore Application Started!");
        System.out.println("Access at: http://localhost:8080");
        System.out.println("========================================");
    }
}