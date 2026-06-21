package com.federicoinnocente.subs_tracker.common.security;

import com.federicoinnocente.subs_tracker.entity.AppUserEntity;
import com.federicoinnocente.subs_tracker.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionContext {

    private final AppUserRepository userRepository;

    public String getEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("Nessuna autenticazione attiva nel contesto");
        }
        return auth.getName();
    }

    public AppUserEntity getUser() {
        String email = getEmail();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User " + email + " non trovato"));
    }

}
