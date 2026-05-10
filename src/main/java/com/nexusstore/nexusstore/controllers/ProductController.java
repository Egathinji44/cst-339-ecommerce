package com.nexusstore.nexusstore.controllers;

import com.nexusstore.nexusstore.models.ProductModel;
import com.nexusstore.nexusstore.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Spring MVC Controller handling all product-related CRUD operations.
 * Delegates all business logic to {@link ProductService} via dependency injection.
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

    // ── LIST ─────────────────────────────────────────────────────────────────

    /**
     * Displays all products, optionally filtered by category.
     *
     * @param category optional category filter parameter
     * @param model    the Spring MVC model
     * @return the products list template
     */
    @GetMapping
    public String listProducts(@RequestParam(required = false) String category, Model model) {
        List<ProductModel> products;
        if (category != null && !category.isBlank()) {
            products = productService.getProductsByCategory(category);
            model.addAttribute("selectedCategory", category);
        } else {
            products = productService.getAllProducts();
        }
        model.addAttribute("products", products);
        model.addAttribute("categories", List.of("Electronics", "Apparel", "Accessories"));
        return "products/list";
    }

    // ── DETAIL ───────────────────────────────────────────────────────────────

    /**
     * Displays the detail view for a single product.
     *
     * @param id    the product ID from the URL path
     * @param model the Spring MVC model
     * @return the product detail template, or redirect to list if product not found
     */
    @GetMapping("/{id}")
    public String viewProduct(@PathVariable int id, Model model) {
        ProductModel product = productService.getProductById(id);
        if (product == null) {
            return "redirect:/products";
        }
        model.addAttribute("product", product);
        return "products/detail";
    }

    // ── CREATE ───────────────────────────────────────────────────────────────

    /**
     * Shows the form for creating a new product.
     *
     * @param model the Spring MVC model
     * @return the product form template
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new ProductModel());
        model.addAttribute("categories", List.of("Electronics", "Apparel", "Accessories"));
        model.addAttribute("formAction", "/products/new");
        model.addAttribute("formTitle", "Add New Product");
        return "products/form";
    }

    /**
     * Processes the create product form submission.
     *
     * @param product       the {@link ProductModel} bound from the form
     * @param bindingResult holds validation errors from {@code @Valid}
     * @param model         the Spring MVC model
     * @return redirect to product list on success, or re-render form on validation failure
     */
    @PostMapping("/new")
    public String createProduct(@Valid @ModelAttribute("product") ProductModel product,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", List.of("Electronics", "Apparel", "Accessories"));
            model.addAttribute("formAction", "/products/new");
            model.addAttribute("formTitle", "Add New Product");
            return "products/form";
        }
        productService.addProduct(product);
        return "redirect:/products";
    }

    // ── EDIT ─────────────────────────────────────────────────────────────────

    /**
     * Shows the form for editing an existing product.
     *
     * @param id    the product ID from the URL path
     * @param model the Spring MVC model
     * @return the product form template pre-filled with existing data
     */
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable int id, Model model) {
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
     *
     * @param id            the product ID from the URL path
     * @param product       the {@link ProductModel} bound from the form
     * @param bindingResult holds validation errors from {@code @Valid}
     * @param model         the Spring MVC model
     * @return redirect to product detail on success, or re-render form on validation failure
     */
    @PostMapping("/{id}/edit")
    public String updateProduct(@PathVariable int id,
                                @Valid @ModelAttribute("product") ProductModel product,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", List.of("Electronics", "Apparel", "Accessories"));
            model.addAttribute("formAction", "/products/" + id + "/edit");
            model.addAttribute("formTitle", "Edit Product");
            return "products/form";
        }
        product.setProductId(id);
        productService.updateProduct(product);
        return "redirect:/products/" + id;
    }

    // ── DELETE ───────────────────────────────────────────────────────────────

    /**
     * Deletes a product by ID and redirects to the product list.
     *
     * @param id the product ID from the URL path
     * @return redirect to product list
     */
    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}
