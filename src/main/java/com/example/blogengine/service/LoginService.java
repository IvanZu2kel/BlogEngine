package com.example.blogengine.service;

import com.example.blogengine.api.request.LoginRequest;
import com.example.blogengine.api.response.security.LoginResponse;
import org.springframework.stereotype.Service;

@Service
public interface LoginService {
    LoginResponse postLogin(LoginRequest loginRequest);
}
