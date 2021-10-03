package com.example.blogengine.api.response;

import com.example.blogengine.model.PostComment;
import lombok.Data;

@Data
public class CommentResponse {
    private final int id;
    private final long timestamp;
    private final String text;
    private final UserPostResponse user;

    public CommentResponse(PostComment postComment) {
        this.id = postComment.getId();
        this.timestamp = postComment.getTime().getTime()/1000;
        this.text = postComment.getText();
        this.user = new UserPostResponse(postComment.getUser());
    }
}
