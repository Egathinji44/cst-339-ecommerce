package com.nexusstore.nexusstore.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Domain model representing a placed customer order in NexusStore.
 *
 * <p>Mapped to the {@code orders} collection. An {@code OrderModel} is
 * created from the contents of a {@link CartModel} at checkout time —
 * line items, prices, and totals are snapshotted into {@link OrderItemModel}
 * entries so the historical record remains accurate even if product data
 * later changes.</p>
 *
 * <p>Milestone 8: Added as part of the Shopping Cart &amp; Ordering module.</p>
 */
@Document(collection = "orders")
public class OrderModel {

    /** Flat shipping fee applied to every order. */
    public static final double SHIPPING_FEE = 5.99;

    /** Primary key – auto-generated MongoDB ObjectId. */
    @Id
    private String id;

    /** Username of the customer who placed the order. */
    @Indexed
    private String username;

    /** Snapshotted line items purchased in this order. */
    private List<OrderItemModel> items;

    /** Shipping address provided at checkout. */
    private ShippingAddress shippingAddress;

    /** Sum of all line totals, before shipping. */
    private Double subtotal;

    /** Flat shipping fee charged for this order. */
    private Double shippingFee;

    /** Final order total ({@code subtotal + shippingFee}). */
    private Double totalAmount;

    /** Current fulfillment status of the order. Defaults to {@link OrderStatus#PENDING}. */
    private OrderStatus status;

    /** Timestamp when the order was placed. */
    private LocalDateTime orderDate;

    /**
     * Default no-arg constructor required by Spring Data MongoDB.
     * Initializes {@code items} to an empty list, {@code status} to
     * {@link OrderStatus#PENDING}, and {@code orderDate} to the current time.
     */
    public OrderModel() {
        this.items = new ArrayList<>();
        this.status = OrderStatus.PENDING;
        this.orderDate = LocalDateTime.now();
    }

    /**
     * Full constructor for creating a new order at checkout time.
     *
     * @param id              unique order ID (null for new records)
     * @param username        username of the purchasing customer
     * @param items           the order's line items
     * @param shippingAddress the shipping address for this order
     * @param subtotal        sum of all line totals, before shipping
     * @param shippingFee     flat shipping fee for this order
     */
    public OrderModel(String id, String username, List<OrderItemModel> items,
                       ShippingAddress shippingAddress, Double subtotal, Double shippingFee) {
        this.id = id;
        this.username = username;
        this.items = (items != null) ? items : new ArrayList<>();
        this.shippingAddress = shippingAddress;
        this.subtotal = subtotal;
        this.shippingFee = shippingFee;
        this.totalAmount = (subtotal != null ? subtotal : 0.0) + (shippingFee != null ? shippingFee : 0.0);
        this.status = OrderStatus.PENDING;
        this.orderDate = LocalDateTime.now();
    }

    /** @return the order's unique ID */
    public String getId() { return id; }

    /** @param id the order's unique ID */
    public void setId(String id) { this.id = id; }

    /** @return the username of the purchasing customer */
    public String getUsername() { return username; }

    /** @param username the username of the purchasing customer */
    public void setUsername(String username) { this.username = username; }

    /** @return the order's line items */
    public List<OrderItemModel> getItems() { return items; }

    /** @param items the order's line items */
    public void setItems(List<OrderItemModel> items) { this.items = items; }

    /** @return the shipping address for this order */
    public ShippingAddress getShippingAddress() { return shippingAddress; }

    /** @param shippingAddress the shipping address for this order */
    public void setShippingAddress(ShippingAddress shippingAddress) { this.shippingAddress = shippingAddress; }

    /** @return the subtotal (sum of line totals, before shipping) */
    public Double getSubtotal() { return subtotal; }

    /** @param subtotal the subtotal */
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

    /** @return the flat shipping fee charged for this order */
    public Double getShippingFee() { return shippingFee; }

    /** @param shippingFee the flat shipping fee */
    public void setShippingFee(Double shippingFee) { this.shippingFee = shippingFee; }

    /** @return the final order total (subtotal + shipping fee) */
    public Double getTotalAmount() { return totalAmount; }

    /** @param totalAmount the final order total */
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    /** @return the current fulfillment status */
    public OrderStatus getStatus() { return status; }

    /** @param status the new fulfillment status */
    public void setStatus(OrderStatus status) { this.status = status; }

    /** @return the timestamp when this order was placed */
    public LocalDateTime getOrderDate() { return orderDate; }

    /** @param orderDate the timestamp when this order was placed */
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }

    /**
     * Computes the total number of individual units across all line items.
     *
     * @return the sum of all line item quantities, or 0 if the order has no items
     */
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.stream()
                .mapToInt(item -> item.getQuantity() == null ? 0 : item.getQuantity())
                .sum();
    }
}
