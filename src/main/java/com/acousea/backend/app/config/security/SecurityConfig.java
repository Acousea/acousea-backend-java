package com.acousea.backend.app.config.security;

import jakarta.annotation.PostConstruct;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${apiPrefix}")
    private String apiPrefix;

    private String[] API_WHITELIST;

    private Environment environment;

    @Autowired
    public SecurityConfig(Environment environment) {
        this.environment = environment;
    }

    @PostConstruct
    public void init() {
        API_WHITELIST = new String[]{
                "/", "/files/**",
                apiPrefix + "/users/auth/register",
                apiPrefix + "/users/auth/login",
                apiPrefix + "/users/auth/logout",
                apiPrefix + "/users/**",
                apiPrefix + "/communication-system/**",
                apiPrefix + "/webhook/rockblock-packets"
        };

        System.out.println("API_WHITELIST: " + List.of(API_WHITELIST));
    }


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
                .csrf(AbstractHttpConfigurer::disable)
                .logout(LogoutConfigurer::permitAll);
        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NotNull CorsRegistry registry) {
                registry.addMapping("/api/**")
//                        .allowedOrigins(environment.getProperty("cors.allowedOrigins"))  // Origenes permitidos
                        .allowedOriginPatterns(environment.getProperty("cors.allowedOriginPatterns"))  // Patrones de origen permitidos
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Métodos HTTP permitidos
                        .allowedHeaders("*")  // Permitir todos los encabezados
                        .allowCredentials(true);  // Permitir el uso de credenciales (cookies, etc.)
            }
        };
    }

}
