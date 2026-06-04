package com.familyleague.service;

import com.familyleague.dto.request.LoginRequest;
import com.familyleague.dto.request.RegisterRequest;
import com.familyleague.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
