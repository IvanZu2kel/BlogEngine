package com.example.blogengine.service;

import com.example.blogengine.api.response.PostsResponse;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

public interface PostService {
    PostsResponse getPosts(int offset, int limit, String mode);

    ResponseEntity<?> getPostsSearch(int offset, int limit, String query);

    ResponseEntity<?> getPostsByDate(int offset, int limit, String date);

    ResponseEntity<?> getPostsByTag(int offset, int limit, String tag);

    ResponseEntity<?> getPostsById(int id, Principal principal);

    ResponseEntity<?> getPostsMy(int offset, int limit, String status, Principal principal);
}
