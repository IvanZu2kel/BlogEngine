package com.example.blogengine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.function.Supplier;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class PostNotFoundException extends Exception {
    public PostNotFoundException(String message) {
        super(message);
    }
}
