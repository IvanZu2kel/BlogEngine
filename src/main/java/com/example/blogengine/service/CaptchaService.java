package com.example.blogengine.service;

import com.example.blogengine.api.response.security.AuthCaptchaResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface CaptchaService {
    AuthCaptchaResponse getCaptcha() throws IOException;
}
