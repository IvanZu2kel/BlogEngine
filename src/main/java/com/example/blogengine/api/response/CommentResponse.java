package com.example.blogengine.api.response;

import com.example.blogengine.model.PostComment;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CommentResponse {
    private int id;
    private long timestamp;
    private String text;
    private UserPostResponse user;
}
