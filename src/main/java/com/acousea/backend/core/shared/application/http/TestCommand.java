package com.acousea.backend.core.shared.application.http;

import com.acousea.backend.core.shared.domain.httpWrappers.Command;
import com.acousea.backend.core.shared.domain.httpWrappers.Result;
import com.acousea.backend.core.shared.infrastructure.services.authentication.AuthenticationService;
import org.springframework.stereotype.Component;


@Component
public class TestCommand extends Command<String, Boolean> {

    AuthenticationService authenticationService;


    public TestCommand(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public Result<Boolean> execute(String username) {
        authenticationService.checkAuthentication("USER");
        return Result.success(true);
    }
}
