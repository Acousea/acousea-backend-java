package com.acousea.backend.app.security;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] API_WHITELIST = {
            "/",
            "/api/v1/users/auth/register",
            "/api/v1/users/auth/login",
            "/api/v1/users/validate-username/**",
            "/api/v1/users/validate-email/**",
            "/api/v1/users/test/**",

    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(API_WHITELIST).permitAll()
                        .anyRequest().authenticated()
                )
//                .formLogin((form) -> form
//                        .loginPage("/login")
//                        .permitAll()
//                )
                .csrf().disable()  // Desactiva la protección CSRF
                .logout(LogoutConfigurer::permitAll);
        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NotNull CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:4200")  // Permitir solicitudes desde localhost:4200
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Métodos HTTP permitidos
                        .allowedHeaders("*")  // Permitir todos los encabezados
                        .allowCredentials(true);  // Permitir el uso de credenciales (cookies, etc.)
            }
        };
    }

}
