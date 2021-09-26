package com.example.blogengine.service;

import com.example.blogengine.api.response.LoginResponse;

import java.security.Principal;

public interface CheckService {
    LoginResponse getCheck(Principal principal);
}
