package com.acousea.backend.core.users.domain.userData;

public record UserAddress(
        String street,
        String city,
        String state,
        String postalCode,
        String country
) {
    public static UserAddress createDefault() {
        return new UserAddress(
                "default",
                "default",
                "default",
                "default",
                "default"
        );
    }
}
