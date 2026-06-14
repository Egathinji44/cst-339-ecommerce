package com.nexusstore.nexusstore.controllers;

import com.nexusstore.nexusstore.models.CartModel;
import com.nexusstore.nexusstore.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Handles viewing and managing the authenticated user's shopping cart.
 *
 * <p>All routes under {@code /cart} require an authenticated user (enforced by
 * {@link com.nexusstore.nexusstore.config.SecurityConfig}). The current
 * username is obtained from the Spring Security {@link Authentication} object
 * and used to look up that user's {@link CartModel} via {@link CartService}.</p>
 *
 * <p>Milestone 8: Added as part of the Shopping Cart &amp; Ordering module.</p>
 */
@Controller
@RequestMapping("/cart")
public class CartController {

    /** Cart service bean injected via constructor DI. */
    private final CartService cartService;

    /**
     * Constructor-based dependency injection of {@link CartService}.
     *
     * @param cartService the cart service bean
     */
    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Displays the current user's shopping cart, including line items,
     * quantities, line totals, and the cart subtotal.
     *
     * @param authentication the current authenticated user
     * @param model          the Spring MVC model
     * @return the cart view template name
     */
    @GetMapping
    public String viewCart(Authentication authentication, Model model) {
        CartModel cart = cartService.getCart(authentication.getName());
        model.addAttribute("cart", cart);
        return "cart/view";
    }

    /**
     * Adds a product to the current user's cart.
     *
     * @param productId          the ID of the product to add
     * @param quantity           the quantity to add (defaults to 1)
     * @param authentication     the current authenticated user
     * @param redirectAttributes used to pass flash messages across the redirect
     * @return redirect to the cart page
     */
    @PostMapping("/add")
    public String addToCart(@RequestParam String productId,
                             @RequestParam(defaultValue = "1") int quantity,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        try {
            cartService.addToCart(authentication.getName(), productId, quantity);
            redirectAttributes.addFlashAttribute("successMessage", "Item added to your cart.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/cart";
    }

    /**
     * Updates the quantity of a line item already in the user's cart.
     * Setting the quantity to zero or less removes the item.
     *
     * @param productId          the ID of the product to update
     * @param quantity           the new quantity for this line item
     * @param authentication     the current authenticated user
     * @param redirectAttributes used to pass flash messages across the redirect
     * @return redirect to the cart page
     */
    @PostMapping("/update")
    public String updateQuantity(@RequestParam String productId,
                                  @RequestParam int quantity,
                                  Authentication authentication,
                                  RedirectAttributes redirectAttributes) {
        try {
            cartService.updateQuantity(authentication.getName(), productId, quantity);
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/cart";
    }

    /**
     * Removes a single line item from the user's cart.
     *
     * @param productId      the ID of the product to remove
     * @param authentication the current authenticated user
     * @return redirect to the cart page
     */
    @PostMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable String productId, Authentication authentication) {
        cartService.removeFromCart(authentication.getName(), productId);
        return "redirect:/cart";
    }

    /**
     * Empties the user's cart entirely.
     *
     * @param authentication the current authenticated user
     * @return redirect to the cart page
     */
    @PostMapping("/clear")
    public String clearCart(Authentication authentication) {
        cartService.clearCart(authentication.getName());
        return "redirect:/cart";
    }
}
