package com.example.blogengine.api.response;

import com.example.blogengine.model.PostComment;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class PostResponseList {
    private long id;
    private long timestamp;
    private UserPostResponse user;
    private String title;
    private String announce;
    private long likeCount;
    private long dislikeCount;
    private long commentCount;
    private long viewCount;
    private boolean active;
    private PostComment comments;
    private List<String> tags;
}
