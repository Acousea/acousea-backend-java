package com.acousea.backend.app.config;

import com.acousea.backend.core.shared.application.utils.PasswordHasher;
import com.acousea.backend.core.shared.infrastructure.utils.passwords.BCryptPasswordHasher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class BaseConfig {

    @Bean
    public PasswordHasher passwordHasher() {
        return new BCryptPasswordHasher();
    }

}