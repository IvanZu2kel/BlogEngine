package com.example.blogengine.controller;

import com.example.blogengine.api.response.LoginResponse;
import com.example.blogengine.service.CheckService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final CheckService checkService;

    public ApiAuthController(CheckService checkService) {
        this.checkService = checkService;
    }

    @GetMapping("/check")
    public ResponseEntity<LoginResponse> check(Principal principal){
        return new ResponseEntity<>(checkService.getCheck(principal), HttpStatus.OK);
    }
}
