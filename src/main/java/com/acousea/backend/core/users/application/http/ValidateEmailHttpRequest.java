package com.acousea.backend.core.users.application.http;

import com.acousea.backend.core.shared.domain.annotations.authentication.RequiresAuthentication;
import com.acousea.backend.core.shared.domain.httpWrappers.HttpRequest;
import com.acousea.backend.core.shared.domain.httpWrappers.HttpResponse;
import com.acousea.backend.core.users.application.ports.UserRepository;
import com.acousea.backend.core.users.domain.User;

public class ValidateUsernameHttpRequest extends HttpRequest<String, Boolean> {
    UserRepository userRepository;

    public ValidateUsernameHttpRequest(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }


    @Override
    @RequiresAuthentication(role = "ADMIN")
    public HttpResponse<Boolean> execute(String username) {
        User user = userRepository.getUser(username);
        if (user == null) {
            return HttpResponse.success(true);
        } else {
            return HttpResponse.fail(401, "User is already taken");
        }
    }
}
