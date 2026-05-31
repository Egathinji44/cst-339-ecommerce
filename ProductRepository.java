package com.nexusstore.repository;  

import com.nexusstore.model.Product;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
    
    @Query("SELECT * FROM products WHERE active = true ORDER BY id DESC")
    List<Product> findAllActive();
    
    Optional<Product> findBySku(String sku);
    
    @Query("SELECT * FROM products WHERE category = :category AND active = true")
    List<Product> findByCategory(@Param("category") String category);
    
    @Query("SELECT * FROM products WHERE (name LIKE %:searchTerm% OR description LIKE %:searchTerm%) AND active = true")
    List<Product> searchProducts(@Param("searchTerm") String searchTerm);
    
    @Query("UPDATE products SET active = :active, updated_at = NOW() WHERE id = :id")
    int updateActiveStatus(@Param("id") Long id, @Param("active") boolean active);
}