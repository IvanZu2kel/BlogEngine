package com.example.blogengine.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class LoginResponse {

    private boolean result;
    @JsonProperty("user")
    private UserLoginResponse userLoginResponse;
}
