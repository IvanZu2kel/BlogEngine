package com.example.blogengine.service;

import com.example.blogengine.api.request.LoginRequest;
import com.example.blogengine.api.response.LoginResponse;

public interface LoginService {
    LoginResponse postLogin(LoginRequest loginRequest);
}
