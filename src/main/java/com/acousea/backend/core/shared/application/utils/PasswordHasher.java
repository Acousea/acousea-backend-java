package com.acousea.backend.core.shared.application.utils;

public interface PasswordHasher {
    String hashPassword(String password);
    boolean checkPassword(String password, String hashedPassword);
}
