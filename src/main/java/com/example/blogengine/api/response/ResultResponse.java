package com.example.blogengine.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResultResponse {
    @JsonProperty("result")
    private final Boolean result;

    public ResultResponse(Boolean result) {
        this.result = setResult(result);
    }

    public Boolean setResult(Boolean result) {
        return result;
    }
}
