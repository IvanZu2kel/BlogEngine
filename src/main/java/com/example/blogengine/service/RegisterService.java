package com.example.blogengine.service;

import com.example.blogengine.api.request.UserRequest;
import com.example.blogengine.api.response.RegisterResponse;

public interface RegisterService {
    RegisterResponse postRegister(UserRequest userRequest);
}
