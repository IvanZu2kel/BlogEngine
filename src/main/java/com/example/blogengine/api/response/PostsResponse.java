package com.example.blogengine.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class PostsResponse {
    @JsonProperty("count")
    private int count;
    @JsonProperty("posts")
    private List<PostResponseList> posts;
}
