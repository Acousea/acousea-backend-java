package com.acousea.backend.core.users.domain.userData;

import java.time.LocalDateTime;

public record UserStatus(
        boolean active,
        boolean emailVerified,
        boolean phoneVerified,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime lastLogin,
        int loginAttempts,
        LocalDateTime passwordChangedAt


) {

    public static UserStatus createDefault() {
        return new UserStatus(
                true,
                false,
                false,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                0,
                LocalDateTime.now()
        );
    }
}
