package com.example.blogengine.service;

import com.example.blogengine.api.request.CommentRequest;
import com.example.blogengine.api.response.comment.CommentResponse;
import com.example.blogengine.exception.CommentNotFoundException;
import com.example.blogengine.exception.PostNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public interface CommentService {
    ResponseEntity<CommentResponse> editComment(CommentRequest commentRequest, Principal principal) throws PostNotFoundException, CommentNotFoundException;
}
