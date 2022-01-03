package com.example.blogengine.api.response.posts;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class PostResponse {
    private long id;
    private long timestamp;
    private boolean active;
    private UserPostResponse user;
    private String title;
    private String text;
    private long likeCount;
    private long dislikeCount;
    private long viewCount;
    private List<CommentResponse> comments;
    private List<String> tags;
}
