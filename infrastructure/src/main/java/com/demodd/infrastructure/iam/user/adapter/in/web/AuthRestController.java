package com.demodd.infrastructure.iam.user.adapter.in.web;

import com.demodd.application.iam.user.dto.AuthTokenResponse;
import com.demodd.application.iam.user.dto.LoginCommand;
import com.demodd.application.iam.user.dto.RegisterUserCommand;
import com.demodd.application.iam.user.dto.UserResponse;
import com.demodd.application.iam.user.port.in.GetUserByUsernameUseCase;
import com.demodd.application.iam.user.port.in.LoginUseCase;
import com.demodd.application.iam.user.port.in.RegisterUserUseCase;
import com.demodd.application.iam.user.port.out.TokenGeneratorPort;
import com.demodd.application.iam.user.service.AuthApplicationService;
import com.demodd.domain.iam.user.port.repository.PasswordEncoderPort;
import com.demodd.domain.iam.user.port.repository.UserRepositoryPort;
import com.demodd.infrastructure.iam.user.adapter.in.web.dto.LoginWebRequest;
import com.demodd.infrastructure.iam.user.adapter.in.web.dto.RegisterWebRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthRestController {

    private final RegisterUserUseCase registerUseCase;
    private final LoginUseCase loginUseCase;
    private final GetUserByUsernameUseCase getUserByUsernameUseCase;

    public AuthRestController(
            UserRepositoryPort userRepository,
            PasswordEncoderPort passwordEncoder,
            TokenGeneratorPort tokenGenerator) {
        AuthApplicationService service = new AuthApplicationService(userRepository, passwordEncoder, tokenGenerator);
        this.registerUseCase = service;
        this.loginUseCase = service;
        this.getUserByUsernameUseCase = service;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterWebRequest request) {
        RegisterUserCommand command = new RegisterUserCommand(
                request.username(),
                request.email(),
                request.password(),
                request.role()
        );
        UserResponse response = registerUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthTokenResponse> login(@Valid @RequestBody LoginWebRequest request) {
        LoginCommand command = new LoginCommand(request.username(), request.password());
        AuthTokenResponse response = loginUseCase.execute(command);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = authentication.getName();
        return ResponseEntity.ok(getUserByUsernameUseCase.execute(username));
    }
}
