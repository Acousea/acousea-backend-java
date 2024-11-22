package com.acousea.backend.core.users.application.http;

import com.acousea.backend.core.shared.application.services.authentication.SessionService;
import com.acousea.backend.core.shared.application.utils.PasswordHasher;
import com.acousea.backend.core.shared.domain.httpWrappers.Command;
import com.acousea.backend.core.shared.domain.httpWrappers.Result;
import com.acousea.backend.core.users.application.http.params.LoginUserParams;
import com.acousea.backend.core.users.application.ports.UserRepository;
import com.acousea.backend.core.users.domain.User;
import jakarta.servlet.http.HttpSession;

public class LoginUserCommand extends Command<LoginUserParams, User> {
    UserRepository userRepository;
    PasswordHasher passwordHasher;
    SessionService sessionService;
    HttpSession session;

    public LoginUserCommand(UserRepository userRepository,
                            PasswordHasher passwordHasher, SessionService sessionService, HttpSession session) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.sessionService = sessionService;
        this.session = session;
    }

    @Override
    public Result<User> execute(LoginUserParams loginUserParams) {
        User user = userRepository.getUser(loginUserParams.getUsername());
        if (user == null) {
            return Result.fail(404, "User not found");
        }
        if (passwordHasher.checkPassword(loginUserParams.getPassword(), user.password())) {
            sessionService.createSession(session.getId(), user);
            return Result.success(user);
        } else {
            return Result.fail(401, "Invalid password");
        }
    }
}
