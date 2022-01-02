package com.example.blogengine.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Map;
import java.util.Set;

@Data
@Accessors(chain = true)
public class CalendarResponse {
    @JsonProperty("years")
    private Integer[] years;
    @JsonProperty("posts")
    private Map<String, Integer> posts;

}
