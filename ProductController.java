package com.nexusstore.controller;

import com.nexusstore.dto.ProductDto;
import com.nexusstore.model.Product;
import com.nexusstore.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @GetMapping
    public String listProducts(Model model,
                               @RequestParam(value = "category", required = false) String category,
                               @RequestParam(value = "search", required = false) String search) {
        
        List<Product> products;
        
        if (search != null && !search.trim().isEmpty()) {
            products = productService.searchProducts(search);
            model.addAttribute("searchTerm", search);
        } else if (category != null && !category.trim().isEmpty()) {
            products = productService.getProductsByCategory(category);
            model.addAttribute("selectedCategory", category);
        } else {
            products = productService.getAllProducts();
        }
        
        model.addAttribute("products", products);
        return "products/list";
    }
    
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new ProductDto());
        return "products/create";
    }
    
    @PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute("product") ProductDto productDto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "products/create";
        }
        
        if (!productService.isSkuAvailable(productDto.getSku())) {
            bindingResult.rejectValue("sku", "error.product", "SKU already exists");
            return "products/create";
        }
        
        try {
            Product created = productService.createProduct(productDto);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Product '" + created.getName() + "' created successfully!");
            return "redirect:/products";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Failed to create product: " + e.getMessage());
            return "redirect:/products/create";
        }
    }
    
    @GetMapping("/{id}")
    public String viewProduct(@PathVariable Long id, Model model) {
        Product product = productService.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        model.addAttribute("product", product);
        return "products/details";
    }
    
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setSku(product.getSku());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setQuantity(product.getQuantity());
        productDto.setCategory(product.getCategory());
        productDto.setImageUrl(product.getImageUrl());
        
        model.addAttribute("product", productDto);
        model.addAttribute("productId", id);
        return "products/update";
    }
    
    @PostMapping("/{id}/edit")
    public String updateProduct(@PathVariable Long id,
                                @Valid @ModelAttribute("product") ProductDto productDto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "products/update";
        }
        
        try {
            Product updated = productService.updateProduct(id, productDto);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Product '" + updated.getName() + "' updated successfully!");
            return "redirect:/products";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Failed to update product: " + e.getMessage());
            return "redirect:/products/" + id + "/edit";
        }
    }
    
    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Failed to delete product: " + e.getMessage());
        }
        return "redirect:/products";
    }
}