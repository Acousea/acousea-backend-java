package com.acousea.backend.core.users.application.http.params;

import com.acousea.backend.core.users.domain.userData.UserAddress;
import com.acousea.backend.core.users.domain.userData.UserInfo;
import com.acousea.backend.core.users.domain.userData.UserProfile;

public class RegisterUserParams {
    private String username;
    private String password;
    private UserInfo personalInfo;
    private UserProfile profile;
    private UserAddress address;

    public RegisterUserParams(String username, String password, UserInfo personalInfo, UserProfile profile, UserAddress address) {
        this.username = username;
        this.password = password;
        this.personalInfo = personalInfo;
        this.profile = profile;
        this.address = address;
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

    public UserInfo getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(UserInfo personalInfo) {
        this.personalInfo = personalInfo;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    public UserAddress getAddress() {
        return address;
    }

    public void setAddress(UserAddress address) {
        this.address = address;
    }
}
