package com.nexusstore.nexusstore.services;

import com.nexusstore.nexusstore.models.CartItemModel;
import com.nexusstore.nexusstore.models.CartModel;
import com.nexusstore.nexusstore.models.OrderItemModel;
import com.nexusstore.nexusstore.models.OrderModel;
import com.nexusstore.nexusstore.models.OrderStatus;
import com.nexusstore.nexusstore.models.ShippingAddress;
import com.nexusstore.nexusstore.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring Data MongoDB implementation of {@link OrderService}.
 *
 * <p>Converts the contents of a user's {@link CartModel} into a persisted
 * {@link OrderModel} at checkout time, decrements product stock for each
 * purchased item via {@link ProductService}, and empties the cart via
 * {@link CartService}.</p>
 *
 * <p>Milestone 8: Added as part of the Shopping Cart &amp; Ordering module.</p>
 */
@Service
public class OrderServiceImpl implements OrderService {

    /** Spring Data MongoDB repository for order persistence, injected via constructor DI. */
    private final OrderRepository orderRepository;

    /** Cart service used to read and empty the user's cart at checkout. */
    private final CartService cartService;

    /** Product service used to decrement stock for purchased items. */
    private final ProductService productService;

    /**
     * Constructor-based dependency injection of {@link OrderRepository},
     * {@link CartService}, and {@link ProductService}.
     *
     * @param orderRepository the order repository bean
     * @param cartService     the cart service bean
     * @param productService  the product service bean
     */
    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, CartService cartService,
                             ProductService productService) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.productService = productService;
    }

    /** {@inheritDoc} */
    @Override
    public OrderModel placeOrder(String username, ShippingAddress shippingAddress) {
        CartModel cart = cartService.getCart(username);
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new IllegalStateException("Your cart is empty. Add some products before checking out.");
        }

        List<OrderItemModel> orderItems = new ArrayList<>();
        for (CartItemModel cartItem : cart.getItems()) {
            orderItems.add(new OrderItemModel(
                    cartItem.getProductId(), cartItem.getProductName(),
                    cartItem.getPrice(), cartItem.getQuantity()));

            // Decrement stock for the purchased quantity.
            productService.decreaseStock(cartItem.getProductId(), cartItem.getQuantity());
        }

        double subtotal = cart.getSubtotal();
        OrderModel order = new OrderModel(null, username, orderItems, shippingAddress,
                subtotal, OrderModel.SHIPPING_FEE);
        order = orderRepository.save(order);

        // Order placed successfully — empty the cart for next time.
        cartService.clearCart(username);

        return order;
    }

    /** {@inheritDoc} */
    @Override
    public List<OrderModel> getOrderHistory(String username) {
        return orderRepository.findByUsernameOrderByOrderDateDesc(username);
    }

    /** {@inheritDoc} */
    @Override
    public OrderModel getOrderById(String id) {
        return orderRepository.findById(id).orElse(null);
    }

    /** {@inheritDoc} */
    @Override
    public List<OrderModel> getAllOrders() {
        return orderRepository.findAllByOrderByOrderDateDesc();
    }

    /** {@inheritDoc} */
    @Override
    public void updateOrderStatus(String id, OrderStatus status) {
        OrderModel order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.setStatus(status);
            orderRepository.save(order);
        }
    }
}
