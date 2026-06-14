package com.nexusstore.nexusstore.repositories;

import com.nexusstore.nexusstore.models.OrderModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data MongoDB repository for {@link OrderModel} persistence.
 *
 * <p>Milestone 8: Added as part of the Shopping Cart &amp; Ordering module.</p>
 */
@Repository
public interface OrderRepository extends MongoRepository<OrderModel, String> {

    /**
     * Returns all orders placed by the given user, most recent first.
     *
     * @param username the username of the customer
     * @return list of matching orders, ordered by {@code orderDate} descending
     */
    List<OrderModel> findByUsernameOrderByOrderDateDesc(String username);

    /**
     * Returns every order in the system, most recent first.
     * Used by the Admin Order Management screen.
     *
     * @return list of all orders, ordered by {@code orderDate} descending
     */
    List<OrderModel> findAllByOrderByOrderDateDesc();
}
