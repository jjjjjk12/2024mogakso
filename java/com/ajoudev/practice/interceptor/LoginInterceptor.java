package com.ajoudev.practice.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        String id = (String) session.getAttribute("id");
        String uri = request.getRequestURI();

        if (id == null) {
            if(!(uri.startsWith("/login") || uri.startsWith("/logout") || uri.startsWith("/register"))) {
                response.sendRedirect("/login");
                return false;
            }
        }

        else {
            if(uri.startsWith("/login") || uri.startsWith("/logout") || uri.startsWith("/register")) {
                response.sendRedirect("/");
                return false;
            }
        }
        return true;
    }
}
