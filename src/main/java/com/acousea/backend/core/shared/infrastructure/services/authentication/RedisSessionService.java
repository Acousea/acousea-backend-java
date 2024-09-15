package com.acousea.backend.core.shared.infrastructure.services.authentication;

import com.acousea.backend.core.shared.application.services.authentication.AuthenticationService;
import com.acousea.backend.core.users.domain.User;
import com.acousea.backend.core.users.domain.constants.UserRole;
import org.springframework.data.redis.core.RedisTemplate;


public class RedisAuthenticationService implements AuthenticationService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String SESSION_PREFIX = "session:";

    public RedisAuthenticationService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean isAuthenticated(String sessionId) {
        // Verifica si la sesión existe en Redis
        return Boolean.TRUE.equals(redisTemplate.hasKey(SESSION_PREFIX + sessionId));
    }

    @Override
    public boolean hasRole(String sessionId, String role) {
        // Obtiene el usuario de la sesión
        User user = (User) redisTemplate.opsForValue().get(SESSION_PREFIX + sessionId);
        if (user != null) {
            return user.role().equals(UserRole.valueOf(role));
        }
        return false;
    }

    @Override
    public void createSession(String id, User user) {
        redisTemplate.opsForValue().set(SESSION_PREFIX + id, user);
    }
}
