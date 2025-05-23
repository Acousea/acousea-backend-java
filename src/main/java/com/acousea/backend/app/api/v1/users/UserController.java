package com.acousea.backend.app.api.v1.users;

import com.acousea.backend.core.shared.application.http.TestCommand;
import com.acousea.backend.core.shared.application.services.authentication.SessionService;
import com.acousea.backend.core.shared.application.utils.PasswordHasher;
import com.acousea.backend.core.shared.domain.httpWrappers.ApiResult;
import com.acousea.backend.core.shared.infrastructure.services.authentication.AuthenticationService;
import com.acousea.backend.core.users.application.http.*;
import com.acousea.backend.core.users.application.http.params.LoginUserParams;
import com.acousea.backend.core.users.application.http.params.RegisterUserParams;
import com.acousea.backend.core.users.application.ports.UserRepository;
import com.acousea.backend.core.users.domain.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${apiPrefix}/users")
public class UserController {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final SessionService sessionService;
    private final AuthenticationService authenticationService;
    private final HttpSession session;

    @Autowired
    public UserController(UserRepository userRepository, PasswordHasher passwordHasher, HttpSession session, SessionService sessionService, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.session = session;
        this.sessionService = sessionService;
        this.authenticationService = authenticationService;
    }


    @PostMapping("/auth/register")
    public ResponseEntity<ApiResult<Boolean>> registerUser(@RequestBody RegisterUserParams registerUserParams) {
        RegisterUserCommand query = new RegisterUserCommand(userRepository, passwordHasher);
        ApiResult<Boolean> response = query.run(registerUserParams);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ApiResult<User>> loginUser(@RequestBody LoginUserParams loginUserParams) {
        LoginUserCommand query = new LoginUserCommand(userRepository, passwordHasher, sessionService, session);
        ApiResult<User> response = query.run(loginUserParams);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<ApiResult<Boolean>> logoutUser() {
        LogoutUserCommand query = new LogoutUserCommand(sessionService, session);
        ApiResult<Boolean> response = query.run(null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate-username")
    public ResponseEntity<ApiResult<Boolean>> validateUsername(@RequestParam String username) {
        ValidateUsernameCommand query = new ValidateUsernameCommand(userRepository);
        ApiResult<Boolean> response = query.run(username);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate-email")
    public ResponseEntity<ApiResult<Boolean>> validateEmail(@RequestParam String email) {
        ValidateEmailCommand query = new ValidateEmailCommand(userRepository);
        ApiResult<Boolean> response = query.run(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test")
    public ResponseEntity<ApiResult<Boolean>> testAdmin() {
        TestCommand query = new TestCommand(authenticationService);
        ApiResult<Boolean> response = query.run(null);
        return ResponseEntity.ok(response);
    }

}
