package com.acousea.backend.core.users.application.http;

import com.acousea.backend.core.shared.domain.httpWrappers.Command;
import com.acousea.backend.core.shared.domain.httpWrappers.ApiResult;
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
    public ApiResult<Boolean> execute(String email) {
        User user = userRepository.getUserByEmail(email);
        if (user == null) {
            return ApiResult.success(true);
        } else {
            return ApiResult.fail(401, "Email is already in use");
        }
    }
}
