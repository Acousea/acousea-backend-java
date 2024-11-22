package com.acousea.backend.core.users.domain.userData;

import com.acousea.backend.core.users.domain.constants.Language;

public record UserProfile(
        String profileImageUrl,
        Language preferredLanguage
) {
    public static UserProfile createDefault() {
        return new UserProfile(
                "default-user.png",
                Language.ENGLISH
        );
    }
}
