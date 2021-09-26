package com.example.blogengine.service;

import com.example.blogengine.api.response.PostsResponse;

public interface PostService {
    PostsResponse getPosts(int offset, int limit, String mode);
}
