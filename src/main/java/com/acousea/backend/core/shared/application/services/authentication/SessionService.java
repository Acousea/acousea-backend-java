package com.acousea.backend.core.shared.application.services.authentication;

import com.acousea.backend.core.users.domain.User;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {
    boolean isAuthenticated(String sessionId);
    boolean hasRole(String sessionId, String role);
    void createSession(String id, User user);
}

