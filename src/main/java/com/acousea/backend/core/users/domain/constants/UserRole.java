package com.acousea.backend.core.users.domain.constants;

public enum UserRole {
    ADMIN, USER, GUEST;

    public static UserRole createDefault() {
        return createAdmin();
    }

    public static UserRole createAdmin() {
        return ADMIN;
    }

    public static UserRole createUser() {
        return USER;
    }

    public static UserRole createGuest() {
        return GUEST;
    }
}
