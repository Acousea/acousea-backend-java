package com.acousea.backend.core.shared.infrastructure.utils.passwords;


import com.acousea.backend.core.shared.application.utils.PasswordHasher;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component("SHA256PasswordHasher")
public class SHA256PasswordHasher implements PasswordHasher {

    @Override
    public String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error while hashing a password", e);
        }
    }

    @Override
    public boolean checkPassword(String password, String hashedPassword) {
        String newHash = hashPassword(password);
        return newHash.equals(hashedPassword);
    }
}
