package com.example.blogengine.service.implementation;

import com.example.blogengine.api.request.UserRequest;
import com.example.blogengine.api.response.RegisterErrorResponse;
import com.example.blogengine.api.response.RegisterResponse;
import com.example.blogengine.repository.CaptchaRepository;
import com.example.blogengine.repository.UserRepository;
import com.example.blogengine.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Date;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CaptchaRepository captchaRepository;


    @Override
    public RegisterResponse postRegister(UserRequest userRequest) {
        boolean result = true;
        RegisterErrorResponse registerErrorResponse = new RegisterErrorResponse();

        if (userRequest.getPassword().length() < 6) {
            registerErrorResponse.setPassword();
            result = false;
        }

        if (userRequest.getEMail().equals(userRepository.findByEmail(userRequest.getEMail()))) {
            registerErrorResponse.setEmail();
            result = false;
        }

        if (!(userRequest.getCaptcha().equals(captchaRepository.checkCaptcha(userRequest.getCaptchaSecret())))) {
            registerErrorResponse.setCaptcha();
            result = false;
        }

        if (!(userRequest.getName().matches("[\\w]+"))) {
            registerErrorResponse.setName();
            result = false;
        }

        if (!result) {
            return new RegisterResponse(false, registerErrorResponse);
        } else {
            userRepository.insertUser(userRequest.getEMail(), userRequest.getName(), new Date(), BCrypt.hashpw(userRequest.getPassword(), BCrypt.gensalt(12)));
            return new RegisterResponse(true);
        }
    }
}
