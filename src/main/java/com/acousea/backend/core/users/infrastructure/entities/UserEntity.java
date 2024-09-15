package com.acousea.backend.core.users.infrastructure;

import com.acousea.backend.core.users.domain.User;
import com.acousea.backend.core.users.domain.constants.UserRole;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class UserEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private String username;

    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_info_id")
    private UserInfoEntity personalInfo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_profile_id")
    private UserProfileEntity profile;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_address_id")
    private UserAddressEntity address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_status_id")
    private UserStatusEntity accountStatus;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    // Getters, setters, and constructors


    public UserEntity(String username, String password, UserInfoEntity personalInfo, UserProfileEntity profile, UserAddressEntity address, UserStatusEntity accountStatus, UserRole role) {
        this.username = username;
        this.password = password;
        this.personalInfo = personalInfo;
        this.profile = profile;
        this.address = address;
        this.accountStatus = accountStatus;
        this.role = role;
    }

    public static UserEntity fromDomain(User user) {
        return new UserEntity(
                user.username(),
                user.password(),
                UserInfoEntity.fromDomain(user.personalInfo()),
                UserProfileEntity.fromDomain(user.profile()),
                UserAddressEntity.fromDomain(user.address()),
                UserStatusEntity.fromDomain(user.accountStatus()),
                user.role()
        );
    }

    public User toDomain() {
        return new User(
                id.toString(),
                username,
                password,
                personalInfo.toDomain(),
                profile.toDomain(),
                address.toDomain(),
                accountStatus.toDomain(),
                role
        );
    }
}
