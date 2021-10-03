package com.example.blogengine.service;

import com.example.blogengine.api.response.PostsResponse;
import org.springframework.http.ResponseEntity;

public interface PostService {
    PostsResponse getPosts(int offset, int limit, String mode);
    ResponseEntity<?> getPostsSearch(int offset, int limit, String query);
}
