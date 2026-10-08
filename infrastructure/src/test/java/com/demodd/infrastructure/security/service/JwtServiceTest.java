package com.demodd.infrastructure.security.service;

import com.demodd.domain.iam.user.model.aggregate.User;
import com.demodd.domain.iam.user.model.valueobject.*;
import com.demodd.infrastructure.security.config.JwtProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private JwtProperties properties;

    @BeforeEach
    void setUp() {
        properties = new JwtProperties();
        properties.setSecretKey("404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
        properties.setExpirationMs(3600000L); // 1 hora
        jwtService = new JwtService(properties);
    }

    @Test
    @DisplayName("Debe generar token JWT válido y extraer username y rol correctamente")
    void shouldGenerateAndValidateToken() {
        User user = User.create(
                Username.of("carlos_admin"),
                UserEmail.of("carlos@clinica.com"),
                PasswordHash.of("$2a$10$hashedpassword"),
                UserRole.admin()
        );

        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertFalse(token.isEmpty());

        String username = jwtService.extractUsername(token);
        assertEquals("carlos_admin", username);

        String role = jwtService.extractRole(token);
        assertEquals("ROLE_ADMIN", role);

        org.springframework.security.core.userdetails.User userDetails =
                new org.springframework.security.core.userdetails.User(
                        "carlos_admin",
                        "password",
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
                );

        assertTrue(jwtService.isTokenValid(token, userDetails));
        assertFalse(jwtService.isTokenExpired(token));
    }

    @Test
    @DisplayName("Debe invalidar token si el usuario no coincide o está alterado")
    void shouldRejectInvalidToken() {
        User user = User.create(
                Username.of("doctor_smith"),
                UserEmail.of("smith@clinica.com"),
                PasswordHash.of("$2a$10$hashedpassword"),
                UserRole.professional()
        );

        String token = jwtService.generateToken(user);

        org.springframework.security.core.userdetails.User otherUser =
                new org.springframework.security.core.userdetails.User(
                        "other_user",
                        "password",
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                );

        assertFalse(jwtService.isTokenValid(token, otherUser));
        assertFalse(jwtService.isTokenValid(token + "tampered", otherUser));
    }
}
