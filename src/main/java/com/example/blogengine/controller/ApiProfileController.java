package com.example.blogengine.controller;

import com.example.blogengine.api.request.ProfileImageRequest;
import com.example.blogengine.api.request.ProfileRequest;
import com.example.blogengine.api.response.profile.ProfileResponse;
import com.example.blogengine.exception.IncorrectFormatException;
import com.example.blogengine.exception.PostNotFoundException;
import com.example.blogengine.exception.StatusNotFoundException;
import com.example.blogengine.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ApiProfileController {
    private final ProfileService profileService;

    @PostMapping(value = "/my", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<ProfileResponse> postProfile(@ModelAttribute ProfileImageRequest profileImageRequest,
                                                       Principal principal) throws StatusNotFoundException, PostNotFoundException, IncorrectFormatException, IOException {
        return new ResponseEntity<>(profileService.postProfile(profileImageRequest, principal), HttpStatus.OK);
    }

    @PostMapping(value = "/my", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<ProfileResponse> postProfile(@RequestBody ProfileRequest profileRequest,
                                                       Principal principal) throws StatusNotFoundException, PostNotFoundException {
        return new ResponseEntity<>(profileService.postProfile(profileRequest, principal), HttpStatus.OK);
    }
}
