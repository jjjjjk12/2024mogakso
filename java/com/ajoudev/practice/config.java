package com.ajoudev.practice;

import com.ajoudev.practice.interceptor.LoginInterceptor;
import com.ajoudev.practice.interceptor.MemberInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class config implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;
    private final MemberInterceptor memberInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] excludeNavBarUrl = {"/logout", "/login"};

        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/logout");

        registry.addInterceptor(memberInterceptor)
                .excludePathPatterns(excludeNavBarUrl);
    }
}
