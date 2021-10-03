package com.example.blogengine.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalenderResponse {

    @JsonProperty("years")
    private Set<String> years;

    @JsonProperty("posts")
    private Map<String, Integer> posts;

}