package com.example.blogengine.service;

import com.example.blogengine.api.response.security.LoginResponse;
import com.example.blogengine.exception.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public interface CheckService {
    LoginResponse getCheck(Principal principal) throws UsernameNotFoundException;
}
