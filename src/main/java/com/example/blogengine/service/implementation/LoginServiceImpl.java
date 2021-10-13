package com.example.blogengine.service.implementation;

import com.example.blogengine.api.request.LoginRequest;
import com.example.blogengine.api.response.LoginResponse;
import com.example.blogengine.api.response.UserLoginResponse;
import com.example.blogengine.model.User;
import com.example.blogengine.repository.UserRepository;
import com.example.blogengine.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @Autowired
    public LoginServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    @Override
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
        UserLoginResponse userLoginResponse = new UserLoginResponse();
        userLoginResponse.setName(user.getName());
        userLoginResponse.setModeration(user.getIsModerator() == 1);
        userLoginResponse.setEmail(user.getEmail());
        userLoginResponse.setId(user.getId());
        return userLoginResponse;
    }

    private LoginResponse getLoginResponse(UserLoginResponse userLoginResponse) {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setResult(true);
        loginResponse.setUserLoginResponse(userLoginResponse);
        return loginResponse;
    }
}
