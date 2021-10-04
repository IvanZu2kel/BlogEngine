package com.example.blogengine.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuthCaptchaResponse {

    @JsonProperty("secret")
    private String secret;

    @JsonProperty("image")
    private String image;
}
