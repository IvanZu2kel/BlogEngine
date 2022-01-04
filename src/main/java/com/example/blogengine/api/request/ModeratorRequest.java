package com.example.blogengine.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ModeratorRequest {
    @JsonProperty(value = "post_id")
    private int postId;
    private String decision;
}
