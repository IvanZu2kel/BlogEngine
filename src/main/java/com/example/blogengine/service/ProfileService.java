package com.example.blogengine.service;

import com.example.blogengine.api.request.ProfileImageRequest;
import com.example.blogengine.api.request.ProfileRequest;
import com.example.blogengine.api.response.profile.ProfileResponse;
import com.example.blogengine.exception.IncorrectFormatException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Principal;

@Service
public interface ProfileService {
    ProfileResponse postProfile(ProfileImageRequest profileImageRequest, Principal principal) throws IncorrectFormatException, IOException;
    ProfileResponse postProfile(ProfileRequest profileImageRequest, Principal principal);
}
