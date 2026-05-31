// src/main/java/com/nexusstore/controller/ErrorController.java
package com.nexusstore.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Custom error controller to handle application errors.
 * 
 * @author NexusStore Team
 * @version 1.0
 */
@Controller
public class CustomErrorController implements ErrorController {
    
    /**
     * Handles all errors and displays appropriate error page.
     * 
     * @param request the HTTP request
     * @param model the Spring Model object
     * @return error view name
     */
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        
        // Get error status code
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object errorMessage = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object requestUri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        
        int statusCode = 0;
        if (status != null) {
            statusCode = Integer.parseInt(status.toString());
        }
        
        // Set error details for display
        model.addAttribute("statusCode", statusCode);
        model.addAttribute("error", getErrorType(statusCode));
        model.addAttribute("message", getErrorMessage(statusCode, errorMessage));
        model.addAttribute("path", requestUri != null ? requestUri.toString() : "/");
        model.addAttribute("timestamp", new java.util.Date());
        
        // Return appropriate error page based on status code
        if (statusCode == HttpStatus.NOT_FOUND.value()) {
            return "error/404";
        } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
            return "error/403";
        } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            return "error/500";
        } else {
            return "error/error";
        }
    }
    
    /**
     * Gets user-friendly error type based on status code.
     */
    private String getErrorType(int statusCode) {
        return switch (statusCode) {
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Page Not Found";
            case 405 -> "Method Not Allowed";
            case 500 -> "Internal Server Error";
            default -> "Application Error";
        };
    }
    
    /**
     * Gets user-friendly error message.
     */
    private String getErrorMessage(int statusCode, Object errorMessage) {
        return switch (statusCode) {
            case 404 -> "The page you are looking for does not exist or has been moved.";
            case 403 -> "You do not have permission to access this resource.";
            case 500 -> "Something went wrong on our end. Please try again later.";
            default -> errorMessage != null ? errorMessage.toString() : "An unexpected error occurred.";
        };
    }
}