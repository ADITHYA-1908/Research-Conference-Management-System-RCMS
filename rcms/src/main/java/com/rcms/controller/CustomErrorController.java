package com.rcms.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int statusCode = 500;
        String errorMessage = "An unexpected server error occurred.";

        if (status != null) {
            statusCode = Integer.parseInt(status.toString());
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                errorMessage = "The requested academic resource or page could not be found (404).";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                errorMessage = "Access Denied: You do not have permission to view this resource (403).";
            } else if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                errorMessage = "Authentication Required: Please sign in to access this portal.";
            }
        }

        model.addAttribute("statusCode", statusCode);
        model.addAttribute("errorMessage", errorMessage);
        return "error";
    }
}
