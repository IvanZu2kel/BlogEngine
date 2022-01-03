package com.example.blogengine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class AuthorAndUserNoEqualsException extends Exception{
    public AuthorAndUserNoEqualsException(String message) {
        super(message);
    }
}
