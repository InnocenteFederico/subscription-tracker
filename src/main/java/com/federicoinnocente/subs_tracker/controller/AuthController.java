package com.federicoinnocente.subs_tracker.controller;

import com.federicoinnocente.subs_tracker.dto.AuthResponse;
import com.federicoinnocente.subs_tracker.dto.LoginRequest;
import com.federicoinnocente.subs_tracker.dto.RegisterRequest;
import com.federicoinnocente.subs_tracker.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }
}
