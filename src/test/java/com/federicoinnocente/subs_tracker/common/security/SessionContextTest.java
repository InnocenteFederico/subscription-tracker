package com.federicoinnocente.subs_tracker.common.security;

import com.federicoinnocente.subs_tracker.entity.AppUserEntity;
import com.federicoinnocente.subs_tracker.repository.AppUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessionContextTest {

    @Mock AppUserRepository userRepository;

    @InjectMocks SessionContext sessionContext;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getEmail_returnsNameFromAuthentication() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("user@example.com", null, List.of()));

        assertEquals("user@example.com", sessionContext.getEmail());
    }

    @Test
    void getEmail_throwsWhenNoAuthentication() {
        SecurityContextHolder.clearContext();

        assertThrows(IllegalStateException.class, () -> sessionContext.getEmail());
    }

    @Test
    void getUser_returnsUserFromRepository() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("user@example.com", null, List.of()));

        AppUserEntity user = new AppUserEntity();
        user.setEmail("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        assertEquals(user, sessionContext.getUser());
    }

    @Test
    void getUser_throwsWhenUserNotFoundInRepository() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("ghost@example.com", null, List.of()));

        when(userRepository.findByEmail("ghost@example.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> sessionContext.getUser());
    }
}
