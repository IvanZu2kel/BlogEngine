package com.example.blogengine.controller;

import com.example.blogengine.api.request.UserRequest;
import com.example.blogengine.api.response.RegisterResponse;
import com.example.blogengine.service.CaptchaService;
import com.example.blogengine.service.CheckService;
import com.example.blogengine.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;


@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final CheckService checkService;
    private final CaptchaService captchaService;
    private final RegisterService registerService;

    @Autowired
    public ApiAuthController(CheckService checkService, CaptchaService captchaService, RegisterService registerService) {
        this.checkService = checkService;
        this.captchaService = captchaService;
        this.registerService = registerService;
    }

    @GetMapping("/check")
    public ResponseEntity<?> check(Principal principal){
        return new ResponseEntity<>(checkService.getCheck(principal), HttpStatus.OK);
    }

    @GetMapping("/captcha")
    public ResponseEntity<?> getCaptcha() throws IOException {
        return new ResponseEntity<>(captchaService.getCaptcha(), HttpStatus.OK);
    }

    @PostMapping("/register")
    public RegisterResponse postRegister(@RequestBody UserRequest userRequest) {
        return registerService.postRegister(userRequest);
    }
}
