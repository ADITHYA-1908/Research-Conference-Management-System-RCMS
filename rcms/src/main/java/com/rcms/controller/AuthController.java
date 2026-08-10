package com.rcms.controller;

import com.rcms.dto.LoginDto;
import com.rcms.dto.RegisterDto;
import com.rcms.entity.User;
import com.rcms.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "registered", required = false) Boolean registered,
                                @RequestParam(value = "logout", required = false) Boolean logout,
                                Model model) {
        model.addAttribute("loginDto", new LoginDto());
        if (error != null) {
            if ("please_login".equals(error)) {
                model.addAttribute("errorMessage", "Please log in to access the system.");
            } else if ("unauthorized".equals(error)) {
                model.addAttribute("errorMessage", "You do not have permission to access that page.");
            } else {
                model.addAttribute("errorMessage", "Invalid email or password.");
            }
        }
        if (Boolean.TRUE.equals(registered)) {
            model.addAttribute("successMessage", "Registration successful! You can now log in.");
        }
        if (Boolean.TRUE.equals(logout)) {
            model.addAttribute("infoMessage", "You have been logged out successfully.");
        }
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@Valid @ModelAttribute("loginDto") LoginDto loginDto,
                               BindingResult bindingResult,
                               HttpSession session,
                               Model model) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        Optional<User> userOptional = userService.authenticate(loginDto.getEmail(), loginDto.getPassword());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            session.setAttribute("user", user);

            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                return "redirect:/admin/dashboard";
            } else if ("AUTHOR".equalsIgnoreCase(user.getRole())) {
                return "redirect:/author/dashboard";
            } else if ("REVIEWER".equalsIgnoreCase(user.getRole())) {
                return "redirect:/reviewer/dashboard";
            } else {
                return "redirect:/";
            }
        } else {
            model.addAttribute("errorMessage", "Invalid email or password.");
            return "login";
        }
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@Valid @ModelAttribute("registerDto") RegisterDto registerDto,
                                  BindingResult bindingResult,
                                  Model model) {
        if (registerDto.getPassword() != null && !registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.registerDto", "Passwords do not match.");
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            userService.registerAuthor(registerDto);
            return "redirect:/login?registered=true";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login?logout=true";
    }
}
