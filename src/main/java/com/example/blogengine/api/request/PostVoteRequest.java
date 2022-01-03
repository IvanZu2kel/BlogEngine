package com.example.blogengine.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PostVoteRequest {
    @JsonProperty("post_id")
    private int postId;
}
