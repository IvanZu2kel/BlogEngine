package com.example.blogengine.service.implementation;

import com.example.blogengine.api.request.ProfileImageRequest;
import com.example.blogengine.api.request.ProfileRequest;
import com.example.blogengine.api.response.profile.ProfileResponse;
import com.example.blogengine.exception.IncorrectFormatException;
import com.example.blogengine.model.User;
import com.example.blogengine.repository.UserRepository;
import com.example.blogengine.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final UserRepository userRepository;
    private final StorageServiceImpl storageService;

    public ProfileResponse postProfile(ProfileImageRequest profileRequest, Principal principal) throws IncorrectFormatException, IOException {
        Map<String, String> errors = new HashMap<>();
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        if (profileRequest.getPassword() != null)
            if (profileRequest.getPassword().length() < 6) {
                errors.put("password", "Пароль короче 6 символов.");
            } else {
                user.setPassword(profileRequest.getPassword());
            }
        if (profileRequest.getEmail() != null)
            if (!user.getEmail().equals(profileRequest.getEmail()))
                if (userRepository.findByEmail(profileRequest.getEmail()).isPresent()) {
                    errors.put("email", "Этот e-mail уже зарегистрирован.");
                } else {
                    user.setEmail(profileRequest.getEmail());
                }
        if (profileRequest.getName() != null)
            if (!user.getName().equals(profileRequest.getName()))
                if (!profileRequest.getName().replaceAll("[0-9a-zA-Zа-яА-Я]", "").equals("")) {
                    errors.put("name", "Имя указано неверно");
                } else {
                    user.setName(profileRequest.getName());
                }
        boolean png = profileRequest.getPhoto().getOriginalFilename().substring(profileRequest.getPhoto().getOriginalFilename().lastIndexOf('.')).equals(".png");
        boolean jpg = profileRequest.getPhoto().getOriginalFilename().substring(profileRequest.getPhoto().getOriginalFilename().lastIndexOf('.')).equals(".jpg");
        if (profileRequest.getPhoto() != null) {
            if (!png && !jpg) {
                errors.put("image", "Отправлен файл не формата изображение jpg, png");
            } else if (profileRequest.getPhoto().getSize() > 5_242_880) {
                errors.put("image", "Размер файла превышает допустимый размер");
            } else user.setPhoto(storageService.storeAvatar(profileRequest.getPhoto()));
        }
        if (errors.isEmpty()) {
            userRepository.save(user);
            return new ProfileResponse().setResult(true);
        } else {
            return new ProfileResponse().setResult(false).setErrors(errors);
        }
    }

    public ProfileResponse postProfile(ProfileRequest profileRequest, Principal principal) {
        Map<String, String> errors = new HashMap<>();
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        if (profileRequest.getPassword() != null)
            if (profileRequest.getPassword().length() < 6) {
                errors.put("password", "Пароль короче 6 символов.");
            } else {
                user.setPassword(profileRequest.getPassword());
            }
        if (profileRequest.getEmail() != null)
            if (!user.getEmail().equals(profileRequest.getEmail()))
                if (userRepository.findByEmail(profileRequest.getEmail()).isPresent()) {
                    errors.put("email", "Этот e-mail уже зарегистрирован.");
                } else {
                    user.setEmail(profileRequest.getEmail());
                }
        if (profileRequest.getName() != null)
            if (!user.getName().equals(profileRequest.getName()))
                if (!profileRequest.getName().replaceAll("[0-9a-zA-Zа-яА-Я]", "").equals("")) {
                    errors.put("name", "Имя указано неверно");
                } else {
                    user.setName(profileRequest.getName());
                }
        if (errors.isEmpty()) {
            user.setPhoto("");
            userRepository.save(user);
            return new ProfileResponse().setResult(true);
        } else {
            return new ProfileResponse().setResult(false).setErrors(errors);
        }
    }
}
