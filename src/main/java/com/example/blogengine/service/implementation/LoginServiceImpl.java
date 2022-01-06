package com.example.blogengine.service.implementation;

import com.example.blogengine.api.request.LoginRequest;
import com.example.blogengine.api.request.PasswordRequest;
import com.example.blogengine.api.request.RestoreRequest;
import com.example.blogengine.api.response.ErrorPassResponse;
import com.example.blogengine.api.response.ResultPassResponse;
import com.example.blogengine.api.response.ResultResponse;
import com.example.blogengine.api.response.security.LoginResponse;
import com.example.blogengine.api.response.security.UserLoginResponse;
import com.example.blogengine.config.MailjetSender;
import com.example.blogengine.config.properties.Url;
import com.example.blogengine.model.CaptchaCodes;
import com.example.blogengine.model.User;
import com.example.blogengine.repository.CaptchaRepository;
import com.example.blogengine.repository.PostRepository;
import com.example.blogengine.repository.UserRepository;
import com.example.blogengine.service.LoginService;
import com.mailjet.client.errors.MailjetException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CaptchaRepository captchaRepository;
    private final MailjetSender sender;
    private final Url url;

    public LoginResponse postLogin(LoginRequest loginRequest) {
        Optional<User> userForLR = userRepository.findByEmail(loginRequest.getEmail());
        if (userForLR.isPresent()) {
            Authentication auth = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(auth);
            User user = getUser(auth);
            UserLoginResponse userLoginResponse = getUserLoginResponse(user);
            return getLoginResponse(userLoginResponse);
        }
        return new LoginResponse().setResult(false);
    }

    public ResultResponse postRestore(RestoreRequest request) throws MailjetException {
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if (user.isPresent()) {
            String code = generateSecretKey();
            user.get().setCode(code);
            userRepository.save(user.get());
            String message = url.getBaseUrl() + "/login/change-password?" + code;
            sender.send(request.getEmail(), message);
            return new ResultResponse().setResult(true);
        } else return new ResultResponse().setResult(false);
    }

    public ResultPassResponse postPassword(PasswordRequest passwordRequest) {
        Optional<User> user = userRepository.findByCode(passwordRequest.getCode());
        CaptchaCodes captchaCodes = captchaRepository.findBySecretCode(passwordRequest.getCaptchaSecret());
        if (passwordRequest.getPassword().length() < 6) {
            return new ResultPassResponse()
                    .setResult(false)
                    .setErrors(new ErrorPassResponse().setPassword("Пароль слишком короткий"));
        }
        if (passwordRequest.getCode().isEmpty()) {
            return new ResultPassResponse()
                    .setResult(false)
                    .setErrors(new ErrorPassResponse()
                            .setCode("Ссылка для восстановления пароля устарела.\n" +
                            "<a href= \n" +
                            "\"" + url.getBaseUrl() + "/login/change-password/" + "\">Запросить ссылку снова</a>"));
        }
        if (!captchaCodes.getCode().equals(passwordRequest.getCaptcha())) {
            return new ResultPassResponse()
                    .setResult(false)
                    .setErrors(new ErrorPassResponse()
                            .setCaptcha("Код с картинки введён неверно"));
        }
        captchaRepository.delete(captchaCodes);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        User currentUser = user.get();
        currentUser
                .setCode(null)
                .setPassword(passwordEncoder.encode(passwordRequest.getPassword()));
        userRepository.save(currentUser);
        return new ResultPassResponse().setResult(true);
    }

    private User getUser(Authentication auth) {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) auth.getPrincipal();
        return userRepository
                .findByEmail(user.getUsername()).orElseThrow(() -> new UsernameNotFoundException(user.getUsername()));
    }

    private UserLoginResponse getUserLoginResponse(User user) {
        UserLoginResponse userLoginResponse = new UserLoginResponse()
                .setId(user.getId())
                .setName(user.getName())
                .setPhoto(user.getPhoto())
                .setEmail(user.getEmail());
        if (user.getIsModerator() == (byte) 1) {
            userLoginResponse.setModeration(true)
                    .setModerationCount(postRepository.findPostByModerationStatus().size())
                    .setSettings(true);
        } else {
            userLoginResponse.setModeration(false)
                    .setModerationCount(0)
                    .setSettings(false);
        }
        return userLoginResponse;
    }

    private LoginResponse getLoginResponse(UserLoginResponse userLoginResponse) {
        return new LoginResponse()
                .setResult(true)
                .setUserLoginResponse(userLoginResponse);
    }

    private String generateSecretKey() {
        return RandomStringUtils.randomAlphanumeric(7);
    }
}
