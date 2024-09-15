package com.acousea.backend.core.shared.domain.annotations.authentication;

public @interface RequiresAuthentication {
    String role() default "";
}
