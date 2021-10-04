package com.example.blogengine.controller;

import com.example.blogengine.service.CaptchaService;
import com.example.blogengine.service.CheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.Principal;


@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final CheckService checkService;
    private final CaptchaService captchaService;

    @Autowired
    public ApiAuthController(CheckService checkService, CaptchaService captchaService) {
        this.checkService = checkService;
        this.captchaService = captchaService;
    }

    @GetMapping("/check")
    public ResponseEntity<?> check(Principal principal){
        return new ResponseEntity<>(checkService.getCheck(principal), HttpStatus.OK);
    }

    @GetMapping("/captcha")
    public ResponseEntity<?> getCaptcha() throws IOException {
        return new ResponseEntity<>(captchaService.getCaptcha(), HttpStatus.OK);
    }
}
