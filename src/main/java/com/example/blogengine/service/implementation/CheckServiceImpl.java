package com.example.blogengine.service.implementation;

import com.example.blogengine.api.response.LoginResponse;
import com.example.blogengine.api.response.UserLoginResponse;
import com.example.blogengine.model.User;
import com.example.blogengine.repository.PostRepository;
import com.example.blogengine.repository.UserRepository;
import com.example.blogengine.service.CheckService;
import org.springframework.stereotype.Service;

import java.security.Principal;


@Service
public class CheckServiceImpl implements CheckService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;


    public CheckServiceImpl(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public LoginResponse getCheck(Principal principal) {
        if (principal == null) {
            return new LoginResponse();
        }

        User currentUser = userRepository.findByEmail(principal.getName()).get();
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setResult(true);
        UserLoginResponse userLoginResponse = new UserLoginResponse();
        userLoginResponse.setId(currentUser.getId());
        userLoginResponse.setName(currentUser.getName());
        userLoginResponse.setPhoto(currentUser.getPhoto());
        userLoginResponse.setEmail(currentUser.getEmail());
        if (currentUser.getIsModerator() == 1) {
            userLoginResponse.setModeration(true);
            userLoginResponse.setModerationCount(postRepository.findPostByModerationStatus().size());
            userLoginResponse.setSettings(true);
        } else {
            userLoginResponse.setSettings(false);
            userLoginResponse.setModerationCount(0);
            userLoginResponse.setSettings(false);
        }
        loginResponse.setUserLoginResponse(userLoginResponse);
        return loginResponse;
    }
}
