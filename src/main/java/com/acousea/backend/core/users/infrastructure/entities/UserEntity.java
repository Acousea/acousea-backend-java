package com.acousea.backend.core.users.infrastructure.entities;

import com.acousea.backend.core.users.domain.User;
import com.acousea.backend.core.users.domain.constants.UserRole;
import com.acousea.backend.core.users.infrastructure.entities.userData.UserAddressEntity;
import com.acousea.backend.core.users.infrastructure.entities.userData.UserInfoEntity;
import com.acousea.backend.core.users.infrastructure.entities.userData.UserProfileEntity;
import com.acousea.backend.core.users.infrastructure.entities.userData.UserStatusEntity;
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

    public UserEntity() {

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
                id,
                username,
                password,
                personalInfo.toDomain(),
                profile.toDomain(),
                address.toDomain(),
                accountStatus.toDomain(),
                role
        );
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserInfoEntity getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(UserInfoEntity personalInfo) {
        this.personalInfo = personalInfo;
    }

    public UserProfileEntity getProfile() {
        return profile;
    }

    public void setProfile(UserProfileEntity profile) {
        this.profile = profile;
    }

    public UserAddressEntity getAddress() {
        return address;
    }

    public void setAddress(UserAddressEntity address) {
        this.address = address;
    }

    public UserStatusEntity getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(UserStatusEntity accountStatus) {
        this.accountStatus = accountStatus;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
