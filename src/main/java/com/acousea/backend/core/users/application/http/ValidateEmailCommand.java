package com.acousea.backend.core.users.application.http;

import com.acousea.backend.core.shared.domain.httpWrappers.Command;
import com.acousea.backend.core.shared.domain.httpWrappers.Result;
import com.acousea.backend.core.users.application.ports.UserRepository;
import com.acousea.backend.core.users.domain.User;

public class ValidateEmailCommand extends Command<String, Boolean> {
    UserRepository userRepository;

    public ValidateEmailCommand(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }


    @Override
    public Result<Boolean> execute(String email) {
        User user = userRepository.getUserByEmail(email);
        if (user == null) {
            return Result.success(true);
        } else {
            return Result.fail(401, "Email is already in use");
        }
    }
}
