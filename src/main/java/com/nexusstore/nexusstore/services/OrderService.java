package com.nexusstore.nexusstore.services;

import com.nexusstore.nexusstore.models.OrderModel;
import com.nexusstore.nexusstore.models.OrderStatus;
import com.nexusstore.nexusstore.models.ShippingAddress;

import java.util.List;

/**
 * Service interface defining business operations for order placement,
 * order history, and admin order management.
 * Implementations are registered as Spring Beans via {@code @Service}.
 *
 * <p>Milestone 8: Added as part of the Shopping Cart &amp; Ordering module.</p>
 */
public interface OrderService {

    /**
     * Places a new order on behalf of the given user using the current
     * contents of their shopping cart.
     *
     * <p>On success: a new {@link OrderModel} is created from a snapshot of
     * the cart's line items, stock quantities are decremented for each
     * purchased product, and the user's cart is emptied.</p>
     *
     * @param username        the username of the purchasing customer
     * @param shippingAddress the shipping address supplied at checkout
     * @return the newly created and persisted {@link OrderModel}
     * @throws IllegalStateException if the user's cart is empty
     */
    OrderModel placeOrder(String username, ShippingAddress shippingAddress);

    /**
     * Returns the order history for the given user, most recent first.
     *
     * @param username the username of the customer
     * @return list of the user's orders, ordered by date descending
     */
    List<OrderModel> getOrderHistory(String username);

    /**
     * Finds a single order by its ID.
     *
     * @param id the order's MongoDB ObjectId string
     * @return the matching {@link OrderModel}, or {@code null} if not found
     */
    OrderModel getOrderById(String id);

    /**
     * Returns every order in the system, most recent first.
     * Used by the Admin Order Management screen.
     *
     * @return list of all orders, ordered by date descending
     */
    List<OrderModel> getAllOrders();

    /**
     * Updates the fulfillment status of an order.
     * Used by the Admin Order Management screen.
     *
     * @param id     the order's MongoDB ObjectId string
     * @param status the new {@link OrderStatus}
     */
    void updateOrderStatus(String id, OrderStatus status);
}
