package com.acousea.backend.core.shared.infrastructure.services.authentication;

import com.acousea.backend.core.shared.application.services.authentication.AuthenticationService;
import com.acousea.backend.core.shared.domain.annotations.authentication.RequiresAuthentication;
import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class AuthenticationAspect {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private HttpSession session;

    @Before("@annotation(com.acousea.backend.core.shared.domain.annotations.authentication.RequiresAuthentication)")
    public void checkAuthentication() throws Throwable {
        String sessionId = session.getId();
        MethodSignature signature = (MethodSignature) MethodSignature.class.cast(MethodSignature.class.cast(joinPoint.getSignature()).getMethod());
        Method method = signature.getMethod();

        RequiresAuthentication annotation = method.getAnnotation(RequiresAuthentication.class);
        String requiredRole = annotation.role();

        if (!authenticationService.isAuthenticated(sessionId)) {
            throw new SecurityException("Usuario no autenticado");
        }

        if (!requiredRole.isEmpty() && !authenticationService.hasRoles(sessionId, requiredRole)) {
            throw new SecurityException("Acceso denegado: se requiere el rol " + requiredRole);
        }
    }
}

