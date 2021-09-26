package com.example.blogengine.service;

import com.example.blogengine.api.response.TagsResponse;

public interface TagService {

    TagsResponse getTags(String query);
}
