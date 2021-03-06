package com.example.blogengine.api.response.tags;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class TagsResponse {
    private Tag4TagsResponse[] tags;
}
