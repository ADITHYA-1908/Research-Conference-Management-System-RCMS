package com.rcms.config;

import com.rcms.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;

        String uri = request.getRequestURI();

        if (currentUser == null) {
            response.sendRedirect("/login?error=please_login");
            return false;
        }

        if (uri.startsWith("/admin") && !"ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            response.sendRedirect("/login?error=unauthorized");
            return false;
        }

        if (uri.startsWith("/author") && !"AUTHOR".equalsIgnoreCase(currentUser.getRole())) {
            response.sendRedirect("/login?error=unauthorized");
            return false;
        }

        if (uri.startsWith("/reviewer") && !"REVIEWER".equalsIgnoreCase(currentUser.getRole())) {
            response.sendRedirect("/login?error=unauthorized");
            return false;
        }

        return true;
    }
}
