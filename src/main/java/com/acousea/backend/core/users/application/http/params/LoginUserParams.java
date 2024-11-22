package com.acousea.backend.core.users.application.http.params;

public class LoginUserParams {
    private final String username;
    private final String password;

    public LoginUserParams(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
