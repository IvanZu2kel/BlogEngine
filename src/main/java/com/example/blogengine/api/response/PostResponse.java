package com.example.blogengine.api.response;

import com.example.blogengine.model.PostComment;
import com.example.blogengine.model.User;
import lombok.Data;

import java.util.List;

@Data
public class PostResponse {

    private String text;
    private long id;
    private long timestamp;
    private User user;
    private String title;
    private long likeCount;
    private long dislikeCount;
    private long viewCount;
    private boolean active;
    private PostComment comments;
    private List<String> tags;
}
