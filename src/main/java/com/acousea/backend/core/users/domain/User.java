package com.acousea.backend.core.users.domain;

import com.acousea.backend.core.users.domain.constants.UserRole;
import com.acousea.backend.core.users.domain.userData.UserAddress;
import com.acousea.backend.core.users.domain.userData.UserInfo;
import com.acousea.backend.core.users.domain.userData.UserProfile;
import com.acousea.backend.core.users.domain.userData.UserStatus;

import java.util.UUID;

public record User(
        UUID id,
        String username,
        String password,
        UserInfo personalInfo,
        UserProfile profile,
        UserAddress address,
        UserStatus accountStatus,
        UserRole role
) {

    public static User createDefault() {
        return new User(
                UUID.randomUUID(),
                "default",
                "$2b$12$TUILMPW4pYtM7mxCkdcJCOvOB09QtB5IIP3OYa72YkGm7S8/btfDq", // 'default' hashed with BCrypt
                UserInfo.createDefault(),
                UserProfile.createDefault(),
                UserAddress.createDefault(),
                UserStatus.createDefault(),
                UserRole.createDefault()
        );
    }

}