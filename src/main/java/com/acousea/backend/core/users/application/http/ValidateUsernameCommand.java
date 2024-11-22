package com.acousea.backend.core.users.application.http;

import com.acousea.backend.core.shared.domain.annotations.authentication.RequiresAuthentication;
import com.acousea.backend.core.shared.domain.httpWrappers.Command;
import com.acousea.backend.core.shared.domain.httpWrappers.Result;
import com.acousea.backend.core.users.application.ports.UserRepository;
import com.acousea.backend.core.users.domain.User;

public class ValidateUsernameCommand extends Command<String, Boolean> {
    UserRepository userRepository;

    public ValidateUsernameCommand(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }


    @Override
    @RequiresAuthentication(role = "ADMIN")
    public Result<Boolean> execute(String username) {
        User user = userRepository.getUser(username);
        if (user == null) {
            return Result.success(true);
        } else {
            return Result.fail(401, "User is already taken");
        }
    }
}
