package com.example.blogengine.api.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ErrorPassResponse {
    private String code;
    private String password;
    private String captcha;
}
