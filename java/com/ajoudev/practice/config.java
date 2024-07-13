package com.ajoudev.practice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class config implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        HandlerInterceptor interceptor = new HandlerInterceptor() {
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
        };
        registry.addInterceptor(interceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/logout");
    }
}
