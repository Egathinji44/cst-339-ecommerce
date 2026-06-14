package com.nexusstore.nexusstore.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Domain model representing a single user's shopping cart in NexusStore.
 *
 * <p>Mapped to the {@code carts} collection. Each {@link UserModel} has at
 * most one {@code CartModel}, identified by the {@code username} field
 * (indexed and unique). The cart holds an embedded list of
 * {@link CartItemModel} line items.</p>
 *
 * <p>Milestone 8: Added as part of the Shopping Cart &amp; Ordering module.</p>
 */
@Document(collection = "carts")
public class CartModel {

    /** Primary key – auto-generated MongoDB ObjectId. */
    @Id
    private String id;

    /** Username of the cart's owner. One cart per user. */
    @Indexed(unique = true)
    private String username;

    /** Line items currently in the cart. */
    private List<CartItemModel> items;

    /**
     * Default no-arg constructor required by Spring Data MongoDB
     * and used when creating a brand-new, empty cart for a user.
     */
    public CartModel() {
        this.items = new ArrayList<>();
    }

    /**
     * Full constructor for programmatic creation.
     *
     * @param id       unique cart ID (null for new records)
     * @param username the owning user's username
     * @param items    the cart's line items
     */
    public CartModel(String id, String username, List<CartItemModel> items) {
        this.id = id;
        this.username = username;
        this.items = (items != null) ? items : new ArrayList<>();
    }

    /** @return the cart's unique ID */
    public String getId() { return id; }

    /** @param id the cart's unique ID */
    public void setId(String id) { this.id = id; }

    /** @return the username of the cart's owner */
    public String getUsername() { return username; }

    /** @param username the username of the cart's owner */
    public void setUsername(String username) { this.username = username; }

    /** @return the cart's line items */
    public List<CartItemModel> getItems() { return items; }

    /** @param items the cart's line items */
    public void setItems(List<CartItemModel> items) { this.items = items; }

    /**
     * Computes the total number of individual units across all line items.
     * Used to display a badge (e.g. "Cart (3)") in the navigation bar.
     *
     * @return the sum of all line item quantities, or 0 if the cart is empty
     */
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.stream()
                .mapToInt(item -> item.getQuantity() == null ? 0 : item.getQuantity())
                .sum();
    }

    /**
     * Computes the subtotal of the cart (sum of all line totals).
     * Not persisted — derived on demand for display purposes.
     *
     * @return the sum of {@code price * quantity} across all line items
     */
    public double getSubtotal() {
        if (items == null) {
            return 0.0;
        }
        return items.stream()
                .mapToDouble(CartItemModel::getLineTotal)
                .sum();
    }
}
