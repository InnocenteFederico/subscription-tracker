package com.federicoinnocente.subs_tracker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    private static final String SECRET = "dGVzdHNlY3JldGtleXRlc3RzZWNyZXRrZXkxMjM0NTY=";
    private static final long EXPIRATION_MS = 3_600_000L;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", SECRET);
        ReflectionTestUtils.setField(jwtService, "expiration", EXPIRATION_MS);
    }

    @Test
    void generatedToken_isValidForSameUser() {
        String username = "test@example.com";
        String token = jwtService.generateToken(username);

        assertTrue(jwtService.isTokenValid(token, username));
    }

    @Test
    void extractUsername_returnsSubjectFromToken() {
        String username = "user@example.com";
        String token = jwtService.generateToken(username);

        assertEquals(username, jwtService.extractUsername(token));
    }

    @Test
    void isTokenValid_returnsFalse_forDifferentUser() {
        String token = jwtService.generateToken("alice@example.com");

        assertFalse(jwtService.isTokenValid(token, "bob@example.com"));
    }
}
