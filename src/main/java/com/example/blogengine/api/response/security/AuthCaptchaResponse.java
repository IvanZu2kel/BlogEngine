package com.example.blogengine.api.response.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AuthCaptchaResponse {
    private String secret;
    private String image;
}
