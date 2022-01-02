package com.example.blogengine.api.response;

import com.example.blogengine.model.Post;
import com.example.blogengine.model.PostComment;
import com.example.blogengine.model.PostVotes;
import com.example.blogengine.model.User;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.time.ZoneId;
import java.util.LinkedList;
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
