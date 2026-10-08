package com.demodd.infrastructure.security.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BCryptPasswordEncoderAdapterTest {

    private BCryptPasswordEncoderAdapter encoder;

    @BeforeEach
    void setUp() {
        encoder = new BCryptPasswordEncoderAdapter();
    }

    @Test
    @DisplayName("Debe codificar contraseñas y verificar coincidencias correctamente")
    void shouldEncodeAndMatchPasswords() {
        String rawPassword = "SecurePassword123!";
        String encoded = encoder.encode(rawPassword);

        assertNotNull(encoded);
        assertNotEquals(rawPassword, encoded);
        assertTrue(encoder.matches(rawPassword, encoded));
        assertFalse(encoder.matches("WrongPassword", encoded));
    }
}
