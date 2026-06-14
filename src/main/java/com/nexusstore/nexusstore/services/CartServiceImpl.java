package com.nexusstore.nexusstore.services;

import com.nexusstore.nexusstore.models.CartItemModel;
import com.nexusstore.nexusstore.models.CartModel;
import com.nexusstore.nexusstore.models.ProductModel;
import com.nexusstore.nexusstore.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Spring Data MongoDB implementation of {@link CartService}.
 *
 * <p>Delegates persistence to {@link CartRepository} and looks up live
 * product data (price, name, image, stock) via {@link ProductService} so
 * cart contents always reflect the current catalog when added or changed.</p>
 *
 * <p>Milestone 8: Added as part of the Shopping Cart &amp; Ordering module.</p>
 */
@Service
public class CartServiceImpl implements CartService {

    /** Spring Data MongoDB repository for cart persistence, injected via constructor DI. */
    private final CartRepository cartRepository;

    /** Product service used to validate stock and snapshot product details. */
    private final ProductService productService;

    /**
     * Constructor-based dependency injection of {@link CartRepository} and
     * {@link ProductService}.
     *
     * @param cartRepository the cart repository bean
     * @param productService the product service bean
     */
    @Autowired
    public CartServiceImpl(CartRepository cartRepository, ProductService productService) {
        this.cartRepository = cartRepository;
        this.productService = productService;
    }

    /**
     * Finds the user's cart, creating and persisting a new empty cart
     * if none exists yet.
     *
     * @param username the username of the cart's owner
     * @return the user's persisted {@link CartModel}
     */
    private CartModel getOrCreateCart(String username) {
        Optional<CartModel> existing = cartRepository.findByUsername(username);
        if (existing.isPresent()) {
            return existing.get();
        }
        CartModel cart = new CartModel(null, username, new ArrayList<>());
        return cartRepository.save(cart);
    }

    /**
     * Finds an existing line item for the given product within a cart.
     *
     * @param cart      the cart to search
     * @param productId the product ID to look for
     * @return the matching {@link CartItemModel}, or {@code null} if not present
     */
    private CartItemModel findItem(CartModel cart, String productId) {
        for (CartItemModel item : cart.getItems()) {
            if (item.getProductId().equals(productId)) {
                return item;
            }
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public CartModel getCart(String username) {
        return getOrCreateCart(username);
    }

    /** {@inheritDoc} */
    @Override
    public void addToCart(String username, String productId, int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1.");
        }

        ProductModel product = productService.getProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("That product could not be found.");
        }

        CartModel cart = getOrCreateCart(username);
        CartItemModel existingItem = findItem(cart, productId);

        int requestedTotal = quantity + (existingItem != null ? existingItem.getQuantity() : 0);
        if (product.getStockQuantity() == null || requestedTotal > product.getStockQuantity()) {
            throw new IllegalArgumentException(
                    "Only " + product.getStockQuantity() + " unit(s) of \"" + product.getName() + "\" are in stock.");
        }

        if (existingItem != null) {
            existingItem.setQuantity(requestedTotal);
            // Refresh the snapshot so price/name/image stay current.
            existingItem.setPrice(product.getPrice());
            existingItem.setProductName(product.getName());
            existingItem.setImageUrl(product.getImageUrl());
        } else {
            cart.getItems().add(new CartItemModel(
                    product.getProductId(), product.getName(), product.getPrice(),
                    product.getImageUrl(), quantity));
        }

        cartRepository.save(cart);
    }

    /** {@inheritDoc} */
    @Override
    public void updateQuantity(String username, String productId, int quantity) {
        CartModel cart = getOrCreateCart(username);
        CartItemModel existingItem = findItem(cart, productId);
        if (existingItem == null) {
            return;
        }

        if (quantity <= 0) {
            cart.getItems().remove(existingItem);
        } else {
            ProductModel product = productService.getProductById(productId);
            if (product != null && product.getStockQuantity() != null && quantity > product.getStockQuantity()) {
                throw new IllegalArgumentException(
                        "Only " + product.getStockQuantity() + " unit(s) of \"" + product.getName() + "\" are in stock.");
            }
            existingItem.setQuantity(quantity);
        }

        cartRepository.save(cart);
    }

    /** {@inheritDoc} */
    @Override
    public void removeFromCart(String username, String productId) {
        CartModel cart = getOrCreateCart(username);
        CartItemModel existingItem = findItem(cart, productId);
        if (existingItem != null) {
            cart.getItems().remove(existingItem);
            cartRepository.save(cart);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void clearCart(String username) {
        CartModel cart = getOrCreateCart(username);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    /** {@inheritDoc} */
    @Override
    public int getItemCount(String username) {
        return cartRepository.findByUsername(username)
                .map(CartModel::getItemCount)
                .orElse(0);
    }
}
