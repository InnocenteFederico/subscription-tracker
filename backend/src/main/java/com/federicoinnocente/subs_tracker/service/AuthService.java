package com.federicoinnocente.subs_tracker.service;

import com.federicoinnocente.subs_tracker.dto.AuthResponse;
import com.federicoinnocente.subs_tracker.dto.LoginRequest;
import com.federicoinnocente.subs_tracker.dto.RegisterRequest;
import com.federicoinnocente.subs_tracker.entity.AppUserEntity;
import com.federicoinnocente.subs_tracker.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AppUserRepository userRepository;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email " + request.getEmail() + " già registrata");
        }
        AppUserEntity user = new AppUserEntity();
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setCreationDate(LocalDateTime.now());
        userRepository.save(user);
        return new AuthResponse(jwtService.generateToken(request.getEmail()));
    }

    public AuthResponse login(LoginRequest request) {
        AppUserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid Password");
        }
        return new AuthResponse(jwtService.generateToken(user.getUsername()));
    }
}
