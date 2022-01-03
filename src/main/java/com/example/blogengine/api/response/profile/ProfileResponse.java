package com.example.blogengine.api.response.profile;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
public class ProfileResponse {
    private boolean result;
    private Map<String, String> errors;
}
