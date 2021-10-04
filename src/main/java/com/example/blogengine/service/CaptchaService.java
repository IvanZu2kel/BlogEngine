package com.example.blogengine.service;

import com.example.blogengine.api.response.AuthCaptchaResponse;

import java.io.IOException;


public interface CaptchaService {
    AuthCaptchaResponse getCaptcha() throws IOException;
}
