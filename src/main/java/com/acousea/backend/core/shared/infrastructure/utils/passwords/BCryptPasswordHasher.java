package com.acousea.backend.core.shared.infrastructure.utils.passwords;

import com.acousea.backend.core.shared.application.utils.PasswordHasher;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;


@Component("BCryptPasswordHasher")
public class BCryptPasswordHasher implements PasswordHasher {

    @Override
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
