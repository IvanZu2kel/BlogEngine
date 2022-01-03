package com.example.blogengine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class ModeratorNotFoundException extends Exception{
    public ModeratorNotFoundException(String message) {
        super(message);
    }
}
