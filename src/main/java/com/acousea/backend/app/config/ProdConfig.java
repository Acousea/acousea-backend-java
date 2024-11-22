package com.acousea.backend.app.config;

import com.acousea.backend.core.communicationSystem.application.ports.CommunicationRequestHistoryRepository;
import com.acousea.backend.core.communicationSystem.infrastructure.ports.SQL.JPACommunicationRequestRepository;
import com.acousea.backend.core.communicationSystem.infrastructure.ports.SQL.SQLCommunicationRequestHistoryRepository;
import com.acousea.backend.core.shared.application.services.StorageService;
import com.acousea.backend.core.shared.application.services.authentication.SessionService;
import com.acousea.backend.core.shared.infrastructure.services.authentication.RedisSessionService;
import com.acousea.backend.core.shared.infrastructure.services.storage.S3StorageService;
import com.acousea.backend.core.users.application.ports.UserRepository;
import com.acousea.backend.core.users.infrastructure.ports.SQL.JPAUserRepository;
import com.acousea.backend.core.users.infrastructure.ports.SQL.SQLUserRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Profile("prod")
public class ProdConfig {

    @Bean
    public UserRepository userRepository(JPAUserRepository jpaUserRepository) {
        return new SQLUserRepository(jpaUserRepository); // Para usar el repositorio SQL
    }

    @Bean
    public SessionService authenticationService() {
        return new RedisSessionService(
                new RedisTemplate<>()
        ); // Usar la implementación basada en Redis para producción
    }

    @Bean
    public StorageService s3StorageService(@Value("${aws.s3.bucket-name}") String bucketName) {
        AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();
        return new S3StorageService(s3Client, bucketName);
    }


    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    public CommunicationRequestHistoryRepository communicationRequestHistoryRepository(JPACommunicationRequestRepository jpaCommunicationRequestRepository) {
        return new SQLCommunicationRequestHistoryRepository(jpaCommunicationRequestRepository);
    }
}
