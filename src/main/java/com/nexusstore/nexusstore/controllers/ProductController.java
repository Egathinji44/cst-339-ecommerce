package com.nexusstore.nexusstore.controllers;

import com.nexusstore.nexusstore.models.ProductModel;
import com.nexusstore.nexusstore.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Handles all product CRUD operations.
 * Write operations (create, edit, delete) are restricted to ADMIN users,
 * checked via Spring Security's SecurityContextHolder (Milestone 6).
 */
@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    // ── LIST / SEARCH ─────────────────────────────────────────────────────────

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

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        if (!isAdmin()) {
            return "redirect:/products";
        }
        model.addAttribute("product", new ProductModel());
        model.addAttribute("categories", List.of("Electronics", "Apparel", "Accessories"));
        model.addAttribute("formAction", "/products/new");
        model.addAttribute("formTitle", "Add New Product");
        return "products/form";
    }

    @PostMapping("/new")
    public String createProduct(@Valid @ModelAttribute("product") ProductModel product,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (!isAdmin()) {
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

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        if (!isAdmin()) {
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

    @PostMapping("/{id}/edit")
    public String updateProduct(@PathVariable String id,
                                @Valid @ModelAttribute("product") ProductModel product,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (!isAdmin()) {
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

    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable String id,
                                RedirectAttributes redirectAttributes) {
        if (!isAdmin()) {
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
