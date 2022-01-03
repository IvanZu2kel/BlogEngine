package com.example.blogengine.service;

import com.example.blogengine.api.request.ModeratorRequest;
import com.example.blogengine.api.request.PostRequest;
import com.example.blogengine.api.response.posts.PostResponse;
import com.example.blogengine.api.response.posts.PostsResponse;
import com.example.blogengine.api.response.posts.ResultResponse;
import com.example.blogengine.exception.AuthorAndUserNoEqualsException;
import com.example.blogengine.exception.PostNotFoundException;
import com.example.blogengine.exception.StatusNotFoundException;
import com.example.blogengine.exception.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public interface PostService {
    PostsResponse getPosts(int offset, int limit, String mode);

    PostsResponse getPostsSearch(int offset, int limit, String query);

    PostsResponse getPostsByDate(int offset, int limit, String date);

    PostsResponse getPostsByTag(int offset, int limit, String tag);

    PostResponse getPostsById(int id, Principal principal) throws UsernameNotFoundException, PostNotFoundException;

    PostsResponse getPostsMy(int offset, int limit, String status, Principal principal) throws StatusNotFoundException;

    PostsResponse getModeratePost(int offset, int limit, String status, Principal principal) throws StatusNotFoundException;

    ResultResponse createPost(PostRequest postRequest, Principal principal);

    ResultResponse putPostsById(int id, PostRequest postRequest, Principal principal) throws PostNotFoundException, AuthorAndUserNoEqualsException;

    ResultResponse postModeratePost(ModeratorRequest moderatorRequest, Principal principal) throws PostNotFoundException;
}
