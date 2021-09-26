package com.example.blogengine.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TagsResponse {

    @JsonProperty("tags")
    private List<TagResponse> tags;

    public void setTags(List<TagResponse> tagResponses) {
        this.tags = tagResponses;
    }
}
