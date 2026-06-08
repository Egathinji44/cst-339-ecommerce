package com.nexusstore.controller;

import com.nexusstore.model.Product;
import com.nexusstore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {
    
    private final ProductService productService;
    
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    
    @GetMapping
    public String listProducts(Model model, HttpSession session) {
        
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("products", productService.findAll());
        model.addAttribute("loggedInUser", session.getAttribute("loggedInUser"));
        return "product/list";
    }
    
    
    @GetMapping("/new")
    public String showCreateForm(Model model, HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("product", new Product());
        model.addAttribute("loggedInUser", session.getAttribute("loggedInUser"));
        return "product/form";
    }
    
    
    @PostMapping("/save")
    public String saveProduct(@Valid @ModelAttribute("product") Product product,
                              BindingResult result,
                              RedirectAttributes redirectAttributes,
                              HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }
        
        if (result.hasErrors()) {
            return "product/form";
        }
        
        productService.save(product);
        redirectAttributes.addFlashAttribute("successMessage", "Product added successfully!");
        return "redirect:/products";
    }
    
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, 
                               RedirectAttributes redirectAttributes, HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }
        
        Optional<Product> product = productService.findById(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            model.addAttribute("loggedInUser", session.getAttribute("loggedInUser"));
            return "product/form";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Product not found");
            return "redirect:/products";
        }
    }
    
    
    @PostMapping("/update")
    public String updateProduct(@Valid @ModelAttribute("product") Product product,
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }
        
        if (result.hasErrors()) {
            return "product/form";
        }
        
        productService.update(product);
        redirectAttributes.addFlashAttribute("successMessage", "Product updated successfully!");
        return "redirect:/products";
    }
    
    
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, 
                                RedirectAttributes redirectAttributes,
                                HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }
        
        if (productService.deleteById(id)) {
            redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Product not found");
        }
        return "redirect:/products";
    }
    
    
    @GetMapping("/view/{id}")
    public String viewProduct(@PathVariable Long id, Model model,
                              RedirectAttributes redirectAttributes, HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }
        
        Optional<Product> product = productService.findById(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            model.addAttribute("loggedInUser", session.getAttribute("loggedInUser"));
            return "product/view";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Product not found");
            return "redirect:/products";
        }
    }
}