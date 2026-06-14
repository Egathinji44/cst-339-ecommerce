package com.nexusstore.nexusstore.models;

/**
 * Represents a single line item within a placed {@link OrderModel}.
 *
 * <p>This is an embedded document, copied from the corresponding
 * {@link CartItemModel} at the moment an order is placed. Storing a
 * permanent snapshot of {@code productName} and {@code price} ensures the
 * order history remains accurate even if a product is later renamed,
 * repriced, or deleted.</p>
 */
public class OrderItemModel {

    /** ID of the {@link ProductModel} this line item refers to. */
    private String productId;

    /** Product name at the time the order was placed. */
    private String productName;

    /** Unit price at the time the order was placed. */
    private Double price;

    /** Number of units purchased. */
    private Integer quantity;

    /**
     * Default no-arg constructor required by Spring Data MongoDB
     * for mapping embedded documents.
     */
    public OrderItemModel() {
    }

    /**
     * Full constructor for creating a new order line item.
     *
     * @param productId   the product's unique ID
     * @param productName the product name (snapshot)
     * @param price       the unit price (snapshot)
     * @param quantity    the number of units purchased
     */
    public OrderItemModel(String productId, String productName, Double price, Integer quantity) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    /** @return the product ID */
    public String getProductId() { return productId; }

    /** @param productId the product ID */
    public void setProductId(String productId) { this.productId = productId; }

    /** @return the product name at the time of purchase */
    public String getProductName() { return productName; }

    /** @param productName the product name */
    public void setProductName(String productName) { this.productName = productName; }

    /** @return the unit price at the time of purchase */
    public Double getPrice() { return price; }

    /** @param price the unit price */
    public void setPrice(Double price) { this.price = price; }

    /** @return the number of units purchased */
    public Integer getQuantity() { return quantity; }

    /** @param quantity the number of units purchased */
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    /**
     * Computes the line total for this item (unit price &times; quantity).
     * Not persisted — derived on demand for display purposes.
     *
     * @return the extended price for this line item
     */
    public Double getLineTotal() {
        if (price == null || quantity == null) {
            return 0.0;
        }
        return price * quantity;
    }
}
