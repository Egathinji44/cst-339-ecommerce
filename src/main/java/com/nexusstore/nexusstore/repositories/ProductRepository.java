package com.nexusstore.nexusstore.repositories;

import com.nexusstore.nexusstore.models.ProductModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JDBC repository for {@link ProductModel} persistence.
 *
 * <p>Extends {@link CrudRepository} to inherit standard CRUD operations:
 * {@code save()}, {@code findById()}, {@code findAll()}, {@code deleteById()}, etc.
 * Spring Boot auto-generates the implementation at runtime via IoC/DI.
 *
 * <p>Custom query methods use Spring Data's derived query naming convention.
 */
@Repository
public interface ProductRepository extends CrudRepository<ProductModel, Integer> {

    /**
     * Returns all products belonging to the given category.
     * Spring Data JDBC derives the SQL from the method name:
     * {@code SELECT * FROM products WHERE category = ?}
     *
     * @param category the category to filter by (case-sensitive)
     * @return list of matching products
     */
    List<ProductModel> findByCategory(String category);
}
