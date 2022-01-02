package com.example.blogengine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UsernameNotFoundException extends Exception{
    public UsernameNotFoundException(String message) {
        super(message);
    }
}
