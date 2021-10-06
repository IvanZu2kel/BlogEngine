package com.example.blogengine.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserRequest {
    @JsonProperty("e_mail")
    private String eMail;
    private String password;
    private String name;
    private String captcha;
    @JsonProperty("captcha_secret")
    private String captchaSecret;
}
