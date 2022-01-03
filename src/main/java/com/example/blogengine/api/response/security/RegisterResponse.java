package com.example.blogengine.api.response.security;

import lombok.Data;

@Data
public class RegisterResponse {

    private final Boolean result;

    private RegisterErrorResponse errors;

    public RegisterResponse(Boolean result) {
        this.result = result;
    }

    public RegisterResponse(Boolean result, RegisterErrorResponse error) {
        this.result = result;
        this.errors = error;
    }
}
