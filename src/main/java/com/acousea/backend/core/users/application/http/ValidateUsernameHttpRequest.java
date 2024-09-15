package com.acousea.backend.core.users.application.http;

import com.acousea.backend.core.shared.application.utils.PasswordHasher;
import com.acousea.backend.core.shared.domain.httpWrappers.HttpRequest;
import com.acousea.backend.core.shared.domain.httpWrappers.HttpResponse;
import com.acousea.backend.core.users.application.http.params.LoginUserParams;
import com.acousea.backend.core.users.application.ports.UserRepository;
import com.acousea.backend.core.users.domain.User;

public class LoginUserHttpRequest extends HttpRequest<LoginUserParams, Boolean> {
    UserRepository userRepository;
    PasswordHasher passwordHasher;

    public LoginUserHttpRequest(UserRepository userRepository,
                                PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public HttpResponse<Boolean> execute(LoginUserParams loginUserParams) {
        User user = userRepository.getUser(loginUserParams.getUsername());
        if (user == null) {
            return new HttpResponse<>(404, "User not found", false);
        }
        if (passwordHasher.checkPassword(loginUserParams.getPassword(), user.password())) {
            return new HttpResponse<>(200, "User logged in successfully", true);
        } else {
            return new HttpResponse<>(401, "Invalid password", false);
        }
    }
}
