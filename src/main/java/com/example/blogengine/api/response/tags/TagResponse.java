package com.example.blogengine.api.response.tags;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TagResponse {

    @JsonProperty("name")
    private final String name;

    @JsonProperty("weight")
    private final double weight;


    public TagResponse(String name, double weight) {
        this.name = name;
        this.weight = weight;
    }
}
