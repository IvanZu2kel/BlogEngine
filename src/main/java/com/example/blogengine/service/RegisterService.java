package com.example.blogengine.service;

import com.example.blogengine.api.request.UserRequest;
import com.example.blogengine.api.response.security.RegisterResponse;
import org.springframework.stereotype.Service;

@Service
public interface RegisterService {
    RegisterResponse postRegister(UserRequest userRequest);
}
