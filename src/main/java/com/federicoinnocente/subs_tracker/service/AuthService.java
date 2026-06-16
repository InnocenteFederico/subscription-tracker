package com.federicoinnocente.subs_tracker.service;

import com.federicoinnocente.subs_tracker.dto.AuthResponse;
import com.federicoinnocente.subs_tracker.dto.LoginRequest;
import com.federicoinnocente.subs_tracker.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
