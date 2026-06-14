package com.nexusstore.nexusstore.models;

/**
 * Represents a single line item within a {@link CartModel}.
 *
 * <p>This is an embedded document — instances are stored inside the
 * {@code items} array of a {@code carts} MongoDB document, not as their
 * own top-level collection.</p>
 *
 * <p>Product details ({@code productName}, {@code price}, {@code imageUrl})
 * are snapshotted at the time the item is added to the cart so the cart
 * page can render without an additional product lookup, and so the price
 * shown to the shopper remains stable while the item sits in their cart.</p>
 */
public class CartItemModel {

    /** ID of the {@link ProductModel} this line item refers to. */
    private String productId;

    /** Product name, snapshotted when added to the cart. */
    private String productName;

    /** Unit price, snapshotted when added to the cart. */
    private Double price;

    /** Product image URL, snapshotted when added to the cart. */
    private String imageUrl;

    /** Number of units of this product in the cart. */
    private Integer quantity;

    /**
     * Default no-arg constructor required by Spring Data MongoDB
     * for mapping embedded documents.
     */
    public CartItemModel() {
    }

    /**
     * Full constructor for creating a new cart line item.
     *
     * @param productId   the product's unique ID
     * @param productName the product name (snapshot)
     * @param price       the unit price (snapshot)
     * @param imageUrl    the product image URL (snapshot)
     * @param quantity    the number of units
     */
    public CartItemModel(String productId, String productName, Double price,
                          String imageUrl, Integer quantity) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    /** @return the product ID */
    public String getProductId() { return productId; }

    /** @param productId the product ID */
    public void setProductId(String productId) { this.productId = productId; }

    /** @return the snapshotted product name */
    public String getProductName() { return productName; }

    /** @param productName the product name */
    public void setProductName(String productName) { this.productName = productName; }

    /** @return the snapshotted unit price */
    public Double getPrice() { return price; }

    /** @param price the unit price */
    public void setPrice(Double price) { this.price = price; }

    /** @return the snapshotted product image URL */
    public String getImageUrl() { return imageUrl; }

    /** @param imageUrl the product image URL */
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    /** @return the quantity of this product in the cart */
    public Integer getQuantity() { return quantity; }

    /** @param quantity the quantity of this product in the cart */
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
