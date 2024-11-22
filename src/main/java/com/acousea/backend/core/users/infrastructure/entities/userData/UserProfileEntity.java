package com.acousea.backend.core.users.infrastructure.entities.userData;

import com.acousea.backend.core.users.domain.constants.Language;
import com.acousea.backend.core.users.domain.userData.UserProfile;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class UserProfileEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private Language preferredLanguage;

    public UserProfileEntity(String profileImageUrl, Language preferredLanguage) {
        this.profileImageUrl = profileImageUrl;
        this.preferredLanguage = preferredLanguage;
    }

    public UserProfileEntity() {

    }

    public static UserProfileEntity fromDomain(UserProfile profile) {
        return new UserProfileEntity(
                profile.profileImageUrl(),
                profile.preferredLanguage()
        );
    }

    public UserProfile toDomain() {
        return new UserProfile(
                profileImageUrl,
                preferredLanguage
        );
    }
}

