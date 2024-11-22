package com.acousea.backend.core.users.domain.userData;

import com.acousea.backend.core.users.domain.constants.Gender;

import java.time.LocalDateTime;

public record UserInfo(
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        LocalDateTime dateOfBirth,
        Gender gender
) {
    public static UserInfo createDefault() {
        return new UserInfo(
                "default",
                "default",
                "default",
                "default",
                LocalDateTime.now(),
                Gender.MALE);
    }
}


