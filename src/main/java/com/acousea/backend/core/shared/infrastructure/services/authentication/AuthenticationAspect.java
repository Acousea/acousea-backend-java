package com.acousea.backend.core.shared.infrastructure.services.authentication;

import com.acousea.backend.core.shared.application.services.authentication.SessionService;
import com.acousea.backend.core.shared.domain.annotations.authentication.RequiresAuthentication;
import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class AuthenticationAspect {

    private final SessionService sessionService;

    private final HttpSession session;

    public AuthenticationAspect(SessionService sessionService, HttpSession session) {
        this.sessionService = sessionService;
        this.session = session;
    }

    @Around("@annotation(requiresAuthentication)")
    public Object checkAuthentication(ProceedingJoinPoint joinPoint, @NotNull RequiresAuthentication requiresAuthentication) throws Throwable {
        String sessionId = session.getId();
        String requiredRole = requiresAuthentication.role();

        // Authentication verification
        if (!sessionService.isAuthenticated(sessionId)) {
            throw new SecurityException("User not authenticated");
        }

        // Role verification
        if (!requiredRole.isEmpty() && !sessionService.hasRole(sessionId, requiredRole)) {
            throw new SecurityException("Access denied. Role " + requiredRole + " is required");
        }

        // Proceed with the method execution
        return joinPoint.proceed();
    }
}
