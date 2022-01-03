package com.example.blogengine.service.implementation;

import com.example.blogengine.api.request.LoginRequest;
import com.example.blogengine.api.response.security.LoginResponse;
import com.example.blogengine.api.response.security.UserLoginResponse;
import com.example.blogengine.model.User;
import com.example.blogengine.repository.PostRepository;
import com.example.blogengine.repository.UserRepository;
import com.example.blogengine.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public LoginResponse postLogin(LoginRequest loginRequest) {
        Authentication auth = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        User user = getUser(auth);
        UserLoginResponse userLoginResponse = getUserLoginResponse(user);
        return getLoginResponse(userLoginResponse);
    }

    private User getUser(Authentication auth) {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) auth.getPrincipal();
        User currentUser = userRepository
                .findByEmail(user.getUsername()).orElseThrow(() -> new UsernameNotFoundException(user.getUsername()));
        return currentUser;
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
}
