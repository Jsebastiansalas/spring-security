package com.demodd.infrastructure.security.service;

import com.demodd.application.iam.user.dto.AuthTokenResponse;
import com.demodd.application.iam.user.dto.LoginCommand;
import com.demodd.application.iam.user.dto.RegisterUserCommand;
import com.demodd.application.iam.user.dto.UserResponse;
import com.demodd.application.iam.user.port.out.TokenGeneratorPort;
import com.demodd.application.iam.user.service.AuthApplicationService;
import com.demodd.domain.iam.user.exception.InvalidCredentialsException;
import com.demodd.domain.iam.user.exception.UserAlreadyExistsException;
import com.demodd.domain.iam.user.model.aggregate.User;
import com.demodd.domain.iam.user.model.valueobject.*;
import com.demodd.domain.iam.user.port.repository.PasswordEncoderPort;
import com.demodd.domain.iam.user.port.repository.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AuthApplicationServiceTest {

    private AuthApplicationService authService;
    private InMemoryUserRepository userRepository;
    private BCryptPasswordEncoderAdapter passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        passwordEncoder = new BCryptPasswordEncoderAdapter();
        TokenGeneratorPort tokenGenerator = new TokenGeneratorPort() {
            @Override
            public String generateToken(User user) {
                return "mock.jwt.token." + user.getUsername().value();
            }

            @Override
            public long getExpirationInSeconds() {
                return 86400L;
            }
        };

        authService = new AuthApplicationService(userRepository, passwordEncoder, tokenGenerator);
    }

    @Test
    @DisplayName("Debe registrar un nuevo usuario exitosamente")
    void shouldRegisterNewUser() {
        RegisterUserCommand command = new RegisterUserCommand(
                "terapeuta1",
                "terapeuta1@clinica.com",
                "Password123!",
                "ROLE_PROFESSIONAL"
        );

        UserResponse response = authService.execute(command);

        assertNotNull(response);
        assertNotNull(response.id());
        assertEquals("terapeuta1", response.username());
        assertEquals("terapeuta1@clinica.com", response.email());
        assertEquals("ROLE_PROFESSIONAL", response.role());
        assertEquals("ACTIVE", response.status());

        assertTrue(userRepository.existsByUsername(Username.of("terapeuta1")));
    }

    @Test
    @DisplayName("Debe lanzar excepción si el nombre de usuario ya existe")
    void shouldThrowWhenUsernameAlreadyExists() {
        RegisterUserCommand command = new RegisterUserCommand(
                "duplicado",
                "primero@clinica.com",
                "Password123!",
                "ROLE_USER"
        );
        authService.execute(command);

        RegisterUserCommand duplicate = new RegisterUserCommand(
                "duplicado",
                "segundo@clinica.com",
                "Password123!",
                "ROLE_USER"
        );

        assertThrows(UserAlreadyExistsException.class, () -> authService.execute(duplicate));
    }

    @Test
    @DisplayName("Debe realizar login exitosamente con credenciales válidas")
    void shouldLoginSuccessfully() {
        authService.execute(new RegisterUserCommand(
                "paciente_juan",
                "juan@correo.com",
                "MiClaveSegura123",
                "ROLE_PATIENT"
        ));

        LoginCommand loginCommand = new LoginCommand("paciente_juan", "MiClaveSegura123");
        AuthTokenResponse authResponse = authService.execute(loginCommand);

        assertNotNull(authResponse);
        assertNotNull(authResponse.token());
        assertEquals("Bearer", authResponse.tokenType());
        assertEquals("paciente_juan", authResponse.user().username());
        assertEquals("ROLE_PATIENT", authResponse.user().role());
    }

    @Test
    @DisplayName("Debe lanzar excepción si las credenciales de login son incorrectas")
    void shouldThrowOnInvalidCredentials() {
        authService.execute(new RegisterUserCommand(
                "usuario_test",
                "test@correo.com",
                "CorrectPassword123",
                "ROLE_USER"
        ));

        LoginCommand invalidLogin = new LoginCommand("usuario_test", "WrongPassword");
        assertThrows(InvalidCredentialsException.class, () -> authService.execute(invalidLogin));
    }

    private static class InMemoryUserRepository implements UserRepositoryPort {
        private final Map<UUID, User> usersById = new HashMap<>();

        @Override
        public User save(User user) {
            usersById.put(user.getId().value(), user);
            return user;
        }

        @Override
        public Optional<User> findById(UserId id) {
            return Optional.ofNullable(usersById.get(id.value()));
        }

        @Override
        public Optional<User> findByUsername(Username username) {
            return usersById.values().stream()
                    .filter(u -> u.getUsername().equals(username))
                    .findFirst();
        }

        @Override
        public Optional<User> findByEmail(UserEmail email) {
            return usersById.values().stream()
                    .filter(u -> u.getEmail().equals(email))
                    .findFirst();
        }

        @Override
        public boolean existsById(UserId id) {
            return usersById.containsKey(id.value());
        }

        @Override
        public boolean existsByUsername(Username username) {
            return usersById.values().stream().anyMatch(u -> u.getUsername().equals(username));
        }

        @Override
        public boolean existsByEmail(UserEmail email) {
            return usersById.values().stream().anyMatch(u -> u.getEmail().equals(email));
        }

        @Override
        public List<User> findAll() {
            return new ArrayList<>(usersById.values());
        }

        @Override
        public void deleteById(UserId id) {
            usersById.remove(id.value());
        }
    }
}
