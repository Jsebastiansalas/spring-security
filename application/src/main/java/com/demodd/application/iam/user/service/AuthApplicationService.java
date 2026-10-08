package com.demodd.application.iam.user.service;

import com.demodd.application.iam.user.dto.AuthTokenResponse;
import com.demodd.application.iam.user.dto.LoginCommand;
import com.demodd.application.iam.user.dto.RegisterUserCommand;
import com.demodd.application.iam.user.dto.UserResponse;
import com.demodd.application.iam.user.port.in.GetUserByIdUseCase;
import com.demodd.application.iam.user.port.in.GetUserByUsernameUseCase;
import com.demodd.application.iam.user.port.in.LoginUseCase;
import com.demodd.application.iam.user.port.in.RegisterUserUseCase;
import com.demodd.application.iam.user.port.out.TokenGeneratorPort;
import com.demodd.domain.iam.user.exception.InvalidCredentialsException;
import com.demodd.domain.iam.user.exception.UserAlreadyExistsException;
import com.demodd.domain.iam.user.exception.UserNotFoundException;
import com.demodd.domain.iam.user.model.aggregate.User;
import com.demodd.domain.iam.user.model.valueobject.*;
import com.demodd.domain.iam.user.port.repository.PasswordEncoderPort;
import com.demodd.domain.iam.user.port.repository.UserRepositoryPort;

import java.util.Objects;
import java.util.UUID;

public class AuthApplicationService implements
        RegisterUserUseCase,
        LoginUseCase,
        GetUserByUsernameUseCase,
        GetUserByIdUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final TokenGeneratorPort tokenGenerator;

    public AuthApplicationService(
            UserRepositoryPort userRepository,
            PasswordEncoderPort passwordEncoder,
            TokenGeneratorPort tokenGenerator) {
        this.userRepository = Objects.requireNonNull(userRepository, "El repositorio no puede ser nulo");
        this.passwordEncoder = Objects.requireNonNull(passwordEncoder, "El codificador de contrasenas no puede ser nulo");
        this.tokenGenerator = Objects.requireNonNull(tokenGenerator, "El generador de tokens no puede ser nulo");
    }

    @Override
    public UserResponse execute(RegisterUserCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("El comando de registro no puede ser nulo");
        }
        if (command.password() == null || command.password().length() < 6) {
            throw new IllegalArgumentException("La contrasena debe tener al menos 6 caracteres");
        }

        Username username = Username.of(command.username());
        UserEmail email = UserEmail.of(command.email());

        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException("El nombre de usuario ya se encuentra registrado: " + username.value());
        }
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("El correo electronico ya se encuentra registrado: " + email.value());
        }

        String encodedHash = passwordEncoder.encode(command.password());
        UserRole role = command.role() != null ? UserRole.of(command.role()) : UserRole.defaultRole();

        User user = User.create(username, email, PasswordHash.of(encodedHash), role);
        User savedUser = userRepository.save(user);

        return UserResponse.fromDomain(savedUser);
    }

    @Override
    public AuthTokenResponse execute(LoginCommand command) {
        if (command == null || command.username() == null || command.password() == null) {
            throw new InvalidCredentialsException();
        }

        Username username = Username.of(command.username());
        User user = userRepository.findByUsername(username)
                .orElseThrow(InvalidCredentialsException::new);

        if (!user.getStatus().isActive()) {
            throw new InvalidCredentialsException("La cuenta de usuario se encuentra inactiva o bloqueada");
        }

        if (!passwordEncoder.matches(command.password(), user.getPasswordHash().value())) {
            throw new InvalidCredentialsException();
        }

        String token = tokenGenerator.generateToken(user);
        return AuthTokenResponse.of(token, tokenGenerator.getExpirationInSeconds(), UserResponse.fromDomain(user));
    }

    @Override
    public UserResponse execute(String username) {
        Username name = Username.of(username);
        return userRepository.findByUsername(name)
                .map(UserResponse::fromDomain)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con nombre: " + username));
    }

    @Override
    public UserResponse execute(UUID id) {
        UserId userId = UserId.of(id);
        return userRepository.findById(userId)
                .map(UserResponse::fromDomain)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
