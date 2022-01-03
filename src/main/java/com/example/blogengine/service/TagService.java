package com.example.blogengine.service;

import com.example.blogengine.api.response.tags.TagsResponse;
import org.springframework.stereotype.Service;

@Service
public interface TagService {
    TagsResponse getTags(String query);
}
