package com.example.blogengine.service.implementation;

import com.example.blogengine.api.response.LoginResponse;
import com.example.blogengine.api.response.UserLoginResponse;
import com.example.blogengine.model.User;
import com.example.blogengine.repository.PostRepository;
import com.example.blogengine.repository.UserRepository;
import com.example.blogengine.service.CheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;


@Service
@RequiredArgsConstructor
public class CheckServiceImpl implements CheckService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public LoginResponse getCheck(Principal principal) {
        if (principal == null) {
            return new LoginResponse()
                    .setResult(false);
        }
        User currentUser = userRepository.findByEmail(principal.getName()).get();
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setResult(true);
        UserLoginResponse userLoginResponse = new UserLoginResponse()
                .setId(currentUser.getId())
                .setName(currentUser.getName())
                .setPhoto(currentUser.getPhoto())
                .setEmail(currentUser.getEmail());
        if (currentUser.getIsModerator() == 1) {
            userLoginResponse.setModeration(true)
                    .setModerationCount(postRepository.findPostByModerationStatus().size())
                    .setSettings(true);
        } else {
            userLoginResponse.setSettings(false)
                    .setModerationCount(0)
                    .setSettings(false);
        }
        loginResponse.setUserLoginResponse(userLoginResponse);
        return loginResponse;
    }
}
