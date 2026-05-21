package com.nexusstore.nexusstore.repositories;

import com.nexusstore.nexusstore.models.ProductModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data MongoDB repository for {@link ProductModel} persistence.
 */
@Repository
public interface ProductRepository extends MongoRepository<ProductModel, String> {

    /**
     * Returns all products belonging to the given category.
     *
     * @param category the category to filter by
     * @return list of matching products
     */
    List<ProductModel> findByCategory(String category);

    /**
     * Returns all products whose name contains the given keyword (case-insensitive).
     *
     * @param keyword the search term
     * @return list of matching products
     */
    List<ProductModel> findByNameContainingIgnoreCase(String keyword);
}