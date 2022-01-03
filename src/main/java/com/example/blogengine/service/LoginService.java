package com.example.blogengine.service;

import com.example.blogengine.api.request.LoginRequest;
import com.example.blogengine.api.response.ResultResponse;
import com.example.blogengine.api.response.security.LoginResponse;
import com.mailjet.client.errors.MailjetException;
import org.springframework.stereotype.Service;

@Service
public interface LoginService {
    LoginResponse postLogin(LoginRequest loginRequest);

    ResultResponse postRestore(String email) throws MailjetException;
}
