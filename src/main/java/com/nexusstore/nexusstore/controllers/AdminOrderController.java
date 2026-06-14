package com.nexusstore.nexusstore.controllers;

import com.nexusstore.nexusstore.models.OrderStatus;
import com.nexusstore.nexusstore.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Handles the Admin Order Management screen: viewing every order placed in
 * the store and updating each order's fulfillment status.
 *
 * <p>All routes under {@code /admin/orders} are restricted to users with the
 * {@code ADMIN} role, enforced by
 * {@link com.nexusstore.nexusstore.config.SecurityConfig}.</p>
 *
 * <p>Milestone 8: Added as part of the Shopping Cart &amp; Ordering module.</p>
 */
@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    /** Order service bean injected via constructor DI. */
    private final OrderService orderService;

    /**
     * Constructor-based dependency injection of {@link OrderService}.
     *
     * @param orderService the order service bean
     */
    @Autowired
    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Displays every order placed in the store, most recent first, along
     * with a status-update control for each.
     *
     * @param model the Spring MVC model
     * @return the admin order management view template name
     */
    @GetMapping
    public String listOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("statuses", List.of(OrderStatus.values()));
        return "admin/orders";
    }

    /**
     * Updates the fulfillment status of an order.
     *
     * @param id                 the order's MongoDB ObjectId string
     * @param status             the new status, submitted as its enum name
     * @param redirectAttributes used to pass a flash confirmation message
     * @return redirect back to the admin order management screen
     */
    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable String id,
                                @RequestParam OrderStatus status,
                                RedirectAttributes redirectAttributes) {
        orderService.updateOrderStatus(id, status);
        redirectAttributes.addFlashAttribute("successMessage", "Order status updated.");
        return "redirect:/admin/orders";
    }
}
