package com.nexusstore.nexusstore.models;

/**
 * Represents the lifecycle status of an {@link OrderModel}.
 *
 * <p>Milestone 8: Introduced as part of the Shopping Cart &amp; Ordering module.
 * Orders are created with {@link #PENDING} status and may be advanced by an
 * administrator through the Admin Order Management screen.</p>
 */
public enum OrderStatus {

    /** Order has been placed by the customer but not yet processed. */
    PENDING,

    /** Order is being prepared / packaged by the store. */
    PROCESSING,

    /** Order has shipped to the customer. */
    SHIPPED,

    /** Order has been delivered to the customer. */
    DELIVERED,

    /** Order was cancelled and will not be fulfilled. */
    CANCELLED
}
