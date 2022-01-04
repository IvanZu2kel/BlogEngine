package com.example.blogengine.api.response.security;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RegisterResponse {
    private Boolean result;
    private RegisterErrorResponse errors;
}
