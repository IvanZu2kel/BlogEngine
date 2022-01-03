package com.example.blogengine.api.response.posts;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ErrorResponse {
    private String title;
    private String text;
}
