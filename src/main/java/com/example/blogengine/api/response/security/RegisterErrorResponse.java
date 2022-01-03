package com.example.blogengine.api.response.security;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RegisterErrorResponse {
    private String email;
    private String name;
    private String password;
    private String captcha;
}
