package com.acousea.backend.app.config;

import com.acousea.backend.core.shared.application.services.StorageService;
import com.acousea.backend.core.shared.application.utils.PasswordHasher;
import com.acousea.backend.core.shared.infrastructure.services.storage.LocalStorageService;
import com.acousea.backend.core.shared.infrastructure.services.storage.S3StorageService;
import com.acousea.backend.core.shared.infrastructure.utils.passwords.BCryptPasswordHasher;
import com.acousea.backend.core.users.application.ports.UserRepository;
import com.acousea.backend.core.users.infrastructure.ports.SQL.JPAUserRepository;
import com.acousea.backend.core.users.infrastructure.ports.SQL.SQLUserRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class AppConfig {

    @Bean
    public UserRepository userRepository(JPAUserRepository jpaUserRepository) {
        return new SQLUserRepository(jpaUserRepository); // Para usar el repositorio SQL
    }

    @Bean
    @Qualifier("PasswordHasher")
    public PasswordHasher passwordHasher() {
        return new BCryptPasswordHasher(); // O cualquier otra implementación de PasswordHasher
    }

    @Bean
    @Profile("dev")
    public StorageService localStorageService(@Value("${storage.local.directory}") String storageDirectory) {
        return new LocalStorageService(storageDirectory);
    }

    @Bean
    @Profile("prod")
    public StorageService s3StorageService(
            @Value("${aws.s3.bucket-name}") String bucketName) {
        AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();
        return new S3StorageService(s3Client, bucketName);
    }
}
