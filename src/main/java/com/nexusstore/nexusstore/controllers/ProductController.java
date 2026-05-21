package com.nexusstore.nexusstore.controllers;

import com.nexusstore.nexusstore.models.ProductModel;
import com.nexusstore.nexusstore.models.UserModel;
import com.nexusstore.nexusstore.services.ProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Spring MVC Controller handling all product-related CRUD operations.
 *
 * <p>Milestone 5 additions:
 * <ul>
 *   <li>Admin-only guard on write operations (create, edit, delete).</li>
 *   <li>Flash messages (success/error) via {@link RedirectAttributes}.</li>
 *   <li>Product search by keyword delegated to {@link ProductService}.</li>
 *   <li>Migrated to MongoDB: all product IDs are now {@code String} (ObjectId).</li>
 * </ul>
 *
 * <p>All business logic is delegated to {@link ProductService} via dependency injection.
 * No business rules exist in this controller; it only handles HTTP routing and view selection.
 */
@Controller
@RequestMapping("/products")
public class ProductController {

    /** Injected ProductService Spring Bean for product business logic. */
    private final ProductService productService;

    /**
     * Constructor-based dependency injection of {@link ProductService}.
     *
     * @param productService the product service bean
     */
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    /**
     * Returns {@code true} if the session contains a logged-in user with the ADMIN role.
     *
     * @param session the current HTTP session
     * @return whether the current user is an admin
     */
    private boolean isAdmin(HttpSession session) {
        UserModel user = (UserModel) session.getAttribute("loggedInUser");
        return user != null && "ADMIN".equals(user.getRole());
    }

    // ── LIST / SEARCH ─────────────────────────────────────────────────────────

    /**
     * Displays all products, optionally filtered by category or searched by keyword.
     * Keyword search takes priority over category filter.
     *
     * @param category optional category filter parameter
     * @param keyword  optional search keyword parameter
     * @param model    the Spring MVC model
     * @return the products list template
     */
    @GetMapping
    public String listProducts(@RequestParam(required = false) String category,
                               @RequestParam(required = false) String keyword,
                               Model model) {
        List<ProductModel> products;

        if (keyword != null && !keyword.isBlank()) {
            products = productService.searchProducts(keyword);
            model.addAttribute("keyword", keyword);
        } else if (category != null && !category.isBlank()) {
            products = productService.getProductsByCategory(category);
            model.addAttribute("selectedCategory", category);
        } else {
            products = productService.getAllProducts();
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", List.of("Electronics", "Apparel", "Accessories"));
        return "products/list";
    }

    // ── DETAIL ────────────────────────────────────────────────────────────────

    /**
     * Displays the detail view for a single product.
     *
     * @param id    the MongoDB ObjectId string from the URL path
     * @param model the Spring MVC model
     * @return the product detail template, or redirect to list if not found
     */
    @GetMapping("/{id}")
    public String viewProduct(@PathVariable String id, Model model) {
        ProductModel product = productService.getProductById(id);
        if (product == null) {
            return "redirect:/products";
        }
        model.addAttribute("product", product);
        return "products/detail";
    }

    // ── CREATE ────────────────────────────────────────────────────────────────

    /**
     * Shows the form for creating a new product.
     * Restricted to ADMIN users; non-admins are redirected to the product list.
     *
     * @param session the HTTP session used to verify admin access
     * @param model   the Spring MVC model
     * @return the product form template, or redirect if unauthorized
     */
    @GetMapping("/new")
    public String showCreateForm(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/products";
        }
        model.addAttribute("product", new ProductModel());
        model.addAttribute("categories", List.of("Electronics", "Apparel", "Accessories"));
        model.addAttribute("formAction", "/products/new");
        model.addAttribute("formTitle", "Add New Product");
        return "products/form";
    }

    /**
     * Processes the create product form submission.
     * Restricted to ADMIN users; non-admins are redirected to the product list.
     *
     * @param product            the {@link ProductModel} bound from the form
     * @param bindingResult      holds validation errors from {@code @Valid}
     * @param session            the HTTP session used to verify admin access
     * @param model              the Spring MVC model
     * @param redirectAttributes used to pass flash messages across the redirect
     * @return redirect to product list on success, or re-render form on validation failure
     */
    @PostMapping("/new")
    public String createProduct(@Valid @ModelAttribute("product") ProductModel product,
                                BindingResult bindingResult,
                                HttpSession session,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/products";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", List.of("Electronics", "Apparel", "Accessories"));
            model.addAttribute("formAction", "/products/new");
            model.addAttribute("formTitle", "Add New Product");
            return "products/form";
        }
        productService.addProduct(product);
        redirectAttributes.addFlashAttribute("successMessage",
                "Product \"" + product.getName() + "\" was added successfully.");
        return "redirect:/products";
    }

    // ── EDIT ──────────────────────────────────────────────────────────────────

    /**
     * Shows the form for editing an existing product.
     * Restricted to ADMIN users; non-admins are redirected to the product detail page.
     *
     * @param id      the MongoDB ObjectId string from the URL path
     * @param session the HTTP session used to verify admin access
     * @param model   the Spring MVC model
     * @return the product form template pre-filled with existing data, or redirect if unauthorized
     */
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/products/" + id;
        }
        ProductModel product = productService.getProductById(id);
        if (product == null) {
            return "redirect:/products";
        }
        model.addAttribute("product", product);
        model.addAttribute("categories", List.of("Electronics", "Apparel", "Accessories"));
        model.addAttribute("formAction", "/products/" + id + "/edit");
        model.addAttribute("formTitle", "Edit Product");
        return "products/form";
    }

    /**
     * Processes the edit product form submission.
     * Restricted to ADMIN users; non-admins are redirected to the product detail page.
     *
     * @param id                 the MongoDB ObjectId string from the URL path
     * @param product            the {@link ProductModel} bound from the form
     * @param bindingResult      holds validation errors from {@code @Valid}
     * @param session            the HTTP session used to verify admin access
     * @param model              the Spring MVC model
     * @param redirectAttributes used to pass flash messages across the redirect
     * @return redirect to product detail on success, or re-render form on validation failure
     */
    @PostMapping("/{id}/edit")
    public String updateProduct(@PathVariable String id,
                                @Valid @ModelAttribute("product") ProductModel product,
                                BindingResult bindingResult,
                                HttpSession session,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/products/" + id;
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", List.of("Electronics", "Apparel", "Accessories"));
            model.addAttribute("formAction", "/products/" + id + "/edit");
            model.addAttribute("formTitle", "Edit Product");
            return "products/form";
        }
        product.setProductId(id);
        productService.updateProduct(product);
        redirectAttributes.addFlashAttribute("successMessage",
                "Product \"" + product.getName() + "\" was updated successfully.");
        return "redirect:/products/" + id;
    }

    // ── DELETE ────────────────────────────────────────────────────────────────

    /**
     * Deletes a product by ID and redirects to the product list.
     * Restricted to ADMIN users; non-admins are redirected to the product detail page.
     *
     * @param id                 the MongoDB ObjectId string from the URL path
     * @param session            the HTTP session used to verify admin access
     * @param redirectAttributes used to pass flash messages across the redirect
     * @return redirect to product list on success, or to detail page if unauthorized
     */
    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable String id,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            return "redirect:/products/" + id;
        }
        ProductModel product = productService.getProductById(id);
        String productName = (product != null) ? product.getName() : "Product";
        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute("successMessage",
                "\"" + productName + "\" was deleted successfully.");
        return "redirect:/products";
    }
}