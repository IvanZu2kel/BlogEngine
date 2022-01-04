package com.example.blogengine.api.request;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class PostRequest {
    private long timestamp;
    private int active;
    private String title;
    private List<String> tags;
    private String text;
}
