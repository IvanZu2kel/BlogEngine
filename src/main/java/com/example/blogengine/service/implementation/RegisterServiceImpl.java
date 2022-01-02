package com.example.blogengine.service.implementation;

import com.example.blogengine.api.request.UserRequest;
import com.example.blogengine.api.response.RegisterErrorResponse;
import com.example.blogengine.api.response.RegisterResponse;
import com.example.blogengine.model.User;
import com.example.blogengine.repository.CaptchaRepository;
import com.example.blogengine.repository.UserRepository;
import com.example.blogengine.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.Date;

@Controller
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {
    private final UserRepository userRepository;
    private final CaptchaRepository captchaRepository;

    public RegisterResponse postRegister(UserRequest userRequest) {
        boolean result = true;
        RegisterErrorResponse registerErrorResponse = new RegisterErrorResponse();
        if (userRequest.getPassword().length() < 6) {
            registerErrorResponse.setPassword("Пароль короче 6-ти символов");
            result = false;
        }
        if (userRequest.getEMail().equals(userRepository.findByEmail(userRequest.getEMail()))) {
            registerErrorResponse.setEmail("Этот e-mail уже зарегистрирован");
            result = false;
        }
        if (!(userRequest.getCaptcha().equals(captchaRepository.checkCaptcha(userRequest.getCaptchaSecret())))) {
            registerErrorResponse.setCaptcha("Код с картинки введён неверно");
            result = false;
        }
        if (!(userRequest.getName().matches("[\\w]+"))) {
            registerErrorResponse.setName("Имя указано неверно, Имя может содержать буквы латинского алфавита, цифры или знак подчеркивания");
            result = false;
        }
        if (!result) {
            return new RegisterResponse(false, registerErrorResponse);
        } else {
            User user = new User()
                    .setEmail(userRequest.getEMail())
                    .setName(userRequest.getName())
                    .setPassword(userRequest.getPassword())
                    .setRegTime(LocalDateTime.now());

            userRepository.save(user);
            return new RegisterResponse(true);
        }
    }
}
