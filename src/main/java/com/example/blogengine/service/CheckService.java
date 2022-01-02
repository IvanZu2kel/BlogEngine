package com.example.blogengine.service;

import com.example.blogengine.api.response.LoginResponse;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public interface CheckService {
    LoginResponse getCheck(Principal principal);
}
