package com.example.blogengine.api.response;

import com.example.blogengine.model.Post;
import com.example.blogengine.model.PostComment;
import com.example.blogengine.model.PostVotes;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
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

    public PostResponseList(Post post) {
        this.id = post.getId();
        this.timestamp = post.getTime().getTime() / 1000;
        this.user = new UserPostResponse(post.getUser());
        this.title = post.getTitle();
        this.announce = setAnnounce(post);
        this.likeCount = getLikeCount(post);
        this.dislikeCount = getDislikeCount(post);
        this.commentCount = setCommentCount(post);
        this.viewCount = getViewCount();
    }

    private long setCommentCount(Post post) {
        if (!(post.getComments() == null)) {
            commentCount = post.getComments().size();
        } else {
            commentCount = 0;
        }

        return commentCount;
    }

    private long getDislikeCount(Post post) {
        dislikeCount = 0;
        if (!(post.getLike() == null)) {
            List<PostVotes> like = new ArrayList<>(post.getLike());
            for (PostVotes l : like) {
                if (l.getValue() == 0) {
                    dislikeCount++;
                }
            }
        }
        return dislikeCount;
    }

    private long getLikeCount(Post post) {
        likeCount = 0;
        if (!(post.getLike() == null)) {
            List<PostVotes> like = new ArrayList<>(post.getLike());
            for (PostVotes l : like) {
                if (l.getValue() == 1) {
                    likeCount++;
                }
            }
        }
        return likeCount;
    }

    private String setAnnounce(Post post) {
        String announce = post.getText()
                .replaceAll("</div>", " ")
                .replaceAll("\\<.*?\\>", "")
                .replaceAll("&nbsp;", " ");

        if (announce.length() > 400) {
            return announce.substring(0, 400) + "...";
        }
        return announce;
    }
}
