package com.nexusstore.nexusstore.repositories;

import com.nexusstore.nexusstore.models.CartModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data MongoDB repository for {@link CartModel} persistence.
 *
 * <p>Milestone 8: Added as part of the Shopping Cart &amp; Ordering module.</p>
 */
@Repository
public interface CartRepository extends MongoRepository<CartModel, String> {

    /**
     * Finds the shopping cart belonging to the given user.
     *
     * @param username the username of the cart's owner
     * @return an {@link Optional} containing the cart if one exists
     */
    Optional<CartModel> findByUsername(String username);
}
