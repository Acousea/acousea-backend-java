package com.acousea.backend.core.users.application.http;

import com.acousea.backend.core.shared.application.utils.PasswordHasher;
import com.acousea.backend.core.shared.domain.httpWrappers.Command;
import com.acousea.backend.core.shared.domain.httpWrappers.ApiResult;
import com.acousea.backend.core.users.application.http.params.RegisterUserParams;
import com.acousea.backend.core.users.application.ports.UserRepository;
import com.acousea.backend.core.users.domain.User;
import com.acousea.backend.core.users.domain.constants.UserRole;
import com.acousea.backend.core.users.domain.userData.UserStatus;

import java.util.UUID;

public class RegisterUserCommand extends Command<RegisterUserParams, Boolean> {
    UserRepository userRepository;
    PasswordHasher passwordHasher;

    public RegisterUserCommand(UserRepository userRepository,
                               PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public ApiResult<Boolean> execute(RegisterUserParams registerUserParams) {
        User newUser = new User(
                UUID.randomUUID(),
                registerUserParams.getUsername(),
                passwordHasher.hashPassword(registerUserParams.getPassword()),
                registerUserParams.getPersonalInfo(),
                registerUserParams.getProfile(),
                registerUserParams.getAddress(),
                UserStatus.createDefault(),
                UserRole.USER // Asignar por defecto el rol USER
        );

        boolean success = userRepository.addUser(newUser);
        if (success) {
            return ApiResult.success(true, "User registered successfully");
        } else {
            return ApiResult.fail(500, "User registration failed");

        }
    }
}
