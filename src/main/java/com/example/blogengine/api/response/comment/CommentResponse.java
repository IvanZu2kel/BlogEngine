package com.example.blogengine.api.response.comment;

import com.example.blogengine.api.response.ErrorResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentResponse {
    private int id;
    private Boolean result;
    private ErrorResponse errors;
}
