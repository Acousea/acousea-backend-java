package com.acousea.backend.core.shared.infrastructure.services.authentication;

import com.acousea.backend.core.shared.application.services.authentication.AuthenticationService;
import com.acousea.backend.core.users.domain.User;
import com.acousea.backend.core.users.domain.constants.UserRole;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class InMemoryAuthenticationService implements AuthenticationService {

    private static final String SESSION_PREFIX = "session:";
    private final ConcurrentMap<String, User> sessions = new ConcurrentHashMap<>();

    @Override
    public boolean isAuthenticated(String sessionId) {
        // Verifica si la sesión existe en el mapa en memoria
        return sessions.containsKey(SESSION_PREFIX + sessionId);
    }

    @Override
    public boolean hasRole(String sessionId, String role) {
        // Obtiene el usuario de la sesión desde el mapa en memoria
        User user = sessions.get(SESSION_PREFIX + sessionId);
        if (user != null) {
            return user.role().equals(UserRole.valueOf(role));
        }
        return false;
    }

    // Métodos adicionales para gestionar las sesiones en memoria (opcional)

    public void createSession(String sessionId, User user) {
        sessions.put(SESSION_PREFIX + sessionId, user);
    }

    public void deleteSession(String sessionId) {
        sessions.remove(SESSION_PREFIX + sessionId);
    }

    public User getUser(String sessionId) {
        return sessions.get(SESSION_PREFIX + sessionId);
    }
}
