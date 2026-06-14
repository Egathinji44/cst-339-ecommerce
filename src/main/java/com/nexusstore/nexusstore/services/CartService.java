package com.nexusstore.nexusstore.services;

import com.nexusstore.nexusstore.models.CartModel;

/**
 * Service interface defining business operations for shopping cart management.
 * Implementations are registered as Spring Beans via {@code @Service}.
 *
 * <p>Milestone 8: Added as part of the Shopping Cart &amp; Ordering module.</p>
 */
public interface CartService {

    /**
     * Returns the shopping cart belonging to the given user, creating a new
     * empty cart if one does not yet exist.
     *
     * @param username the username of the cart's owner
     * @return the user's {@link CartModel}, never {@code null}
     */
    CartModel getCart(String username);

    /**
     * Adds the given quantity of a product to the user's cart. If the product
     * is already in the cart, the quantities are combined. Validates that the
     * requested quantity does not exceed the product's available stock.
     *
     * @param username  the username of the cart's owner
     * @param productId the ID of the product to add
     * @param quantity  the number of units to add (must be &ge; 1)
     * @throws IllegalArgumentException if the product does not exist, the
     *         quantity is not positive, or insufficient stock is available
     */
    void addToCart(String username, String productId, int quantity);

    /**
     * Updates the quantity of an existing line item in the user's cart.
     * If {@code quantity} is zero or negative, the item is removed entirely.
     *
     * @param username  the username of the cart's owner
     * @param productId the ID of the product to update
     * @param quantity  the new quantity for this line item
     * @throws IllegalArgumentException if the requested quantity exceeds
     *         the product's available stock
     */
    void updateQuantity(String username, String productId, int quantity);

    /**
     * Removes a line item from the user's cart entirely.
     *
     * @param username  the username of the cart's owner
     * @param productId the ID of the product to remove
     */
    void removeFromCart(String username, String productId);

    /**
     * Removes all line items from the user's cart.
     *
     * @param username the username of the cart's owner
     */
    void clearCart(String username);

    /**
     * Returns the total number of individual units in the user's cart,
     * used to render a badge (e.g. "Cart (3)") in the navigation bar.
     *
     * @param username the username of the cart's owner
     * @return the total quantity of items in the cart, or 0 if empty
     */
    int getItemCount(String username);
}
