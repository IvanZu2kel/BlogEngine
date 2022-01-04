package com.example.blogengine.api.response.tags;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Tag4TagsResponse {
    @JsonProperty("name")
    private String name;
    @JsonProperty("weight")
    private double weight;
}
