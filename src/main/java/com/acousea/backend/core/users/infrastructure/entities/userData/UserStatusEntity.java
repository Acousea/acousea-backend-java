package com.acousea.backend.core.users.infrastructure.entities.userData;


import com.acousea.backend.core.users.domain.userData.UserStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class UserStatusEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private boolean active;

    private boolean emailVerified;

    private boolean phoneVerified;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime lastLogin;

    private int loginAttempts;

    private LocalDateTime passwordChangedAt;

    // Getters, setters, and constructors


    public UserStatusEntity(boolean active, boolean emailVerified, boolean phoneVerified, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime lastLogin, int loginAttempts, LocalDateTime passwordChangedAt) {
        this.active = active;
        this.emailVerified = emailVerified;
        this.phoneVerified = phoneVerified;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lastLogin = lastLogin;
        this.loginAttempts = loginAttempts;
        this.passwordChangedAt = passwordChangedAt;
    }

    public UserStatusEntity() {

    }

    public static UserStatusEntity fromDomain(UserStatus status) {
        return new UserStatusEntity(
                status.active(),
                status.emailVerified(),
                status.phoneVerified(),
                status.createdAt(),
                status.updatedAt(),
                status.lastLogin(),
                status.loginAttempts(),
                status.passwordChangedAt()
        );
    }

    public UserStatus toDomain() {
        return new UserStatus(
                active,
                emailVerified,
                phoneVerified,
                createdAt,
                updatedAt,
                lastLogin,
                loginAttempts,
                passwordChangedAt
        );
    }
}

