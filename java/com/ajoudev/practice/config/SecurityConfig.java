package com.ajoudev.practice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain (HttpSecurity security) throws Exception {

        security.authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/register").permitAll()
                .anyRequest().authenticated());

        security.sessionManagement(session -> session
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true));
        security.sessionManagement(session -> session
                .sessionFixation().changeSessionId());

        security.logout(auth -> auth
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login"));

        security.formLogin(auth -> auth
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .usernameParameter("id")
                .passwordParameter("pw")
                .failureUrl("/login?error=true"));

        security.csrf(auth -> auth.disable());

        return security.build();
    }

}
