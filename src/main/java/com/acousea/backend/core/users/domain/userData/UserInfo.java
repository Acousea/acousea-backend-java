package com.acousea.backend.core.users.domain.userData;

import com.acousea.backend.core.users.domain.constants.Gender;

import java.time.LocalDateTime;

public record PersonalInfo(
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        LocalDateTime dateOfBirth,
        Gender gender
) {}


