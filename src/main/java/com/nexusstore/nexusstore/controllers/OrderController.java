package com.nexusstore.nexusstore.controllers;

import com.nexusstore.nexusstore.models.CartModel;
import com.nexusstore.nexusstore.models.OrderModel;
import com.nexusstore.nexusstore.models.ShippingAddress;
import com.nexusstore.nexusstore.services.CartService;
import com.nexusstore.nexusstore.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Handles the checkout process, order placement, and order history /
 * order detail views for the authenticated user.
 *
 * <p>All routes here require an authenticated user (enforced by
 * {@link com.nexusstore.nexusstore.config.SecurityConfig}). The order
 * detail page is also accessible to administrators viewing any user's
 * order, via the {@code hasRole('ADMIN')} check below.</p>
 *
 * <p>Milestone 8: Added as part of the Shopping Cart &amp; Ordering module.</p>
 */
@Controller
public class OrderController {

    /** Cart service bean injected via constructor DI. */
    private final CartService cartService;

    /** Order service bean injected via constructor DI. */
    private final OrderService orderService;

    /**
     * Constructor-based dependency injection of {@link CartService} and
     * {@link OrderService}.
     *
     * @param cartService  the cart service bean
     * @param orderService the order service bean
     */
    @Autowired
    public OrderController(CartService cartService, OrderService orderService) {
        this.cartService = cartService;
        this.orderService = orderService;
    }

    /**
     * Returns {@code true} if the given authentication has the {@code ADMIN} role.
     *
     * @param authentication the current authenticated user
     * @return {@code true} if the user has {@code ROLE_ADMIN}
     */
    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    /**
     * Displays the checkout page: a read-only summary of the user's cart
     * alongside a shipping address form. Redirects back to the cart if it
     * is currently empty.
     *
     * @param authentication     the current authenticated user
     * @param model              the Spring MVC model
     * @param redirectAttributes used to pass a flash message if redirected
     * @return the checkout view template name, or a redirect to the cart
     */
    @GetMapping("/checkout")
    public String showCheckout(Authentication authentication, Model model,
                                RedirectAttributes redirectAttributes) {
        CartModel cart = cartService.getCart(authentication.getName());
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Your cart is empty. Add some products before checking out.");
            return "redirect:/cart";
        }
        model.addAttribute("cart", cart);
        model.addAttribute("shippingAddress", new ShippingAddress());
        return "checkout";
    }

    /**
     * Processes the checkout form submission: validates the shipping
     * address, converts the user's cart into a new {@link OrderModel},
     * decrements product stock, and empties the cart.
     *
     * @param shippingAddress    the shipping address bound from the form
     * @param bindingResult      holds validation errors from {@code @Valid}
     * @param authentication     the current authenticated user
     * @param model              the Spring MVC model (used if validation fails)
     * @param redirectAttributes used to pass flash messages across the redirect
     * @return redirect to the new order's detail page on success, the
     *         checkout page again if validation fails, or back to the
     *         cart if the cart turned out to be empty
     */
    @PostMapping("/checkout")
    public String placeOrder(@Valid @ModelAttribute("shippingAddress") ShippingAddress shippingAddress,
                              BindingResult bindingResult,
                              Authentication authentication,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            CartModel cart = cartService.getCart(authentication.getName());
            model.addAttribute("cart", cart);
            return "checkout";
        }

        try {
            OrderModel order = orderService.placeOrder(authentication.getName(), shippingAddress);
            redirectAttributes.addFlashAttribute("orderPlaced", true);
            return "redirect:/orders/" + order.getId();
        } catch (IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/cart";
        }
    }

    /**
     * Displays the current user's order history, most recent first.
     *
     * @param authentication the current authenticated user
     * @param model          the Spring MVC model
     * @return the order history view template name
     */
    @GetMapping("/orders")
    public String orderHistory(Authentication authentication, Model model) {
        model.addAttribute("orders", orderService.getOrderHistory(authentication.getName()));
        return "orders/history";
    }

    /**
     * Displays the details of a single order, including its line items,
     * shipping address, status, and totals.
     *
     * <p>Access is limited to the order's owner or an administrator;
     * any other user is redirected back to their own order history.</p>
     *
     * @param id             the order's MongoDB ObjectId string
     * @param authentication the current authenticated user
     * @param model          the Spring MVC model
     * @return the order detail view template name, or a redirect if the
     *         order does not exist or does not belong to this user
     */
    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable String id, Authentication authentication, Model model) {
        OrderModel order = orderService.getOrderById(id);
        if (order == null) {
            return "redirect:/orders";
        }
        if (!order.getUsername().equals(authentication.getName()) && !isAdmin(authentication)) {
            return "redirect:/orders";
        }
        model.addAttribute("order", order);
        return "orders/detail";
    }
}
