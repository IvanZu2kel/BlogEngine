package com.example.blogengine.service.implementation;

import com.example.blogengine.api.request.UserRequest;
import com.example.blogengine.api.response.security.RegisterErrorResponse;
import com.example.blogengine.api.response.security.RegisterResponse;
import com.example.blogengine.exception.MultiuserModeException;
import com.example.blogengine.model.User;
import com.example.blogengine.repository.CaptchaRepository;
import com.example.blogengine.repository.GlobalSettingsRepository;
import com.example.blogengine.repository.UserRepository;
import com.example.blogengine.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

@Controller
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {
    private final UserRepository userRepository;
    private final CaptchaRepository captchaRepository;
    private final GlobalSettingsRepository globalSettingsRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public RegisterResponse postRegister(UserRequest userRequest) throws MultiuserModeException {
        if (globalSettingsRepository.findAllGlobalSettings("MULTIUSER_MODE").getValue().equals("NO")) {
            throw new MultiuserModeException();
        }
        RegisterErrorResponse registerErrorResponse = new RegisterErrorResponse();
        if (userRequest.getPassword().length() < 6) {
            registerErrorResponse.setPassword("Пароль короче 6-ти символов");
            return new RegisterResponse().setResult(false).setErrors(registerErrorResponse);
        }
        if (userRepository.findByEmail(userRequest.getEMail()).isPresent()) {
            registerErrorResponse.setEmail("Этот e-mail уже зарегистрирован");
            return new RegisterResponse().setResult(false).setErrors(registerErrorResponse);
        }
        if (!(userRequest.getCaptcha().equals(captchaRepository.checkCaptcha(userRequest.getCaptchaSecret())))) {
            registerErrorResponse.setCaptcha("Код с картинки введён неверно");
            return new RegisterResponse().setResult(false).setErrors(registerErrorResponse);
        }
        if (!(userRequest.getName().matches("[A-Za-zА-Яа-я0-9_]+"))) {
            registerErrorResponse.setName("Имя указано неверно. Имя может содержать буквы латинского алфавита, цифры или знак подчеркивания");
            return new RegisterResponse().setResult(false).setErrors(registerErrorResponse);
        } else {
            User user = new User()
                    .setEmail(userRequest.getEMail())
                    .setName(userRequest.getName())
                    .setPassword(encoder.encode(userRequest.getPassword()))
                    .setRegTime(new Date())
                    .setIsModerator(0);
            userRepository.save(user);
            return new RegisterResponse().setResult(true);
        }
    }
}
