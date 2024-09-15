package com.acousea.backend.core.shared.infrastructure.services.authentication;

import com.acousea.backend.core.shared.application.services.authentication.SessionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationChecker {

    private final SessionService sessionService;
    private final HttpSession session;

    public AuthenticationChecker(SessionService sessionService, HttpSession session) {
        this.sessionService = sessionService;
        this.session = session;
    }

    public void checkAuthentication(String requiredRole) {
        String sessionId = session.getId();

        // Authentication verification
        if (!sessionService.isAuthenticated(sessionId)) {
            throw new SecurityException("User not authenticated");
        }

        // Role verification
        if (!requiredRole.isEmpty() && !sessionService.hasRole(sessionId, requiredRole)) {
            throw new SecurityException("Access denied. Role " + requiredRole + " is required");
        }
    }
}
