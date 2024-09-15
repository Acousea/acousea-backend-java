package com.acousea.backend.core.users.application.http;

import com.acousea.backend.core.shared.domain.httpWrappers.HttpRequest;
import com.acousea.backend.core.shared.domain.httpWrappers.HttpResponse;
import com.acousea.backend.core.users.application.http.params.RegisterUserParams;
import com.acousea.backend.core.users.application.ports.UserRepository;
import com.acousea.backend.core.users.domain.User;
import com.acousea.backend.core.users.domain.constants.UserRole;
import com.acousea.backend.core.users.domain.userData.UserStatus;

import java.util.UUID;

public class RegisterUserHttpRequest extends HttpRequest<RegisterUserParams, Boolean> {
    UserRepository userRepository;

    public RegisterUserHttpRequest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public HttpResponse<Boolean> execute(RegisterUserParams registerUserParams) {
        User newUser = new User(
                UUID.randomUUID().toString(),
                registerUserParams.getUsername(),
                registerUserParams.getPassword(),
                registerUserParams.getPersonalInfo(),
                registerUserParams.getProfile(),
                registerUserParams.getAddress(),
                UserStatus.createDefault(),
                UserRole.USER // Asignar por defecto el rol USER
        );

        boolean success = userRepository.addUser(newUser);
        if (success) {
            return new HttpResponse<>(200, "User registered successfully", true);
        } else {
            return new HttpResponse<>(500, "User registration failed", false);
        }
    }
}
