package com.example.blogengine.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PostsResponse {
    @JsonProperty("count")
    private int count;
    @JsonProperty("posts")
    private List<PostResponseList> posts;
}
