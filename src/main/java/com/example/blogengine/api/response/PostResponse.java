package com.example.blogengine.api.response;

import com.example.blogengine.model.Post;
import com.example.blogengine.model.PostComment;
import com.example.blogengine.model.PostVotes;
import com.example.blogengine.model.User;
import lombok.Data;

import java.time.Instant;
import java.time.ZoneId;
import java.util.LinkedList;
import java.util.List;

@Data
public class PostResponse {

    private String text;
    private long id;
    private long timestamp;
    private UserPostResponse user;
    private String title;
    private long likeCount;
    private long dislikeCount;
    private long viewCount;
    private boolean active;
    private List<CommentResponse> comments;
    private List<String> tags;

    public PostResponse(List<CommentResponse> comments, Post post, List<String> tags) {
        this.id = post.getId();
        this.timestamp = post.getTime().toInstant().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()/1000;
        this.active = setActive(post.getIsActive());
        this.user = new UserPostResponse().setName(post.getUser().getName()).setId(post.getUser().getId());
        this.title = post.getTitle();
        this.text = post.getText();
        this.likeCount = getLikeCount(post);
        this.dislikeCount = getDislikeCount(post);
        this.viewCount = post.getViewCount();
        this.comments = comments;
        this.tags = tags;
    }

    public boolean setActive(int a) {
        this.active = a == 1;
        return this.active;
    }

    public long getLikeCount(Post post) {
        likeCount = 0;

        if (!(post.getLike() == null)) {
            LinkedList<PostVotes> like = new LinkedList<>(post.getLike());
            for (PostVotes l : like
            ) {
                if (l.getValue() == 1) {
                    likeCount++;
                }

            }
        }
        return likeCount;
    }

    public long getDislikeCount(Post post) {
        dislikeCount = 0;

        if (!(post.getLike() == null)) {
            LinkedList<PostVotes> like = new LinkedList<>(post.getLike());
            for (PostVotes l : like
            ) {
                if (l.getValue() == 0) {
                    dislikeCount++;
                }

            }
        }
        return dislikeCount;
    }

}
