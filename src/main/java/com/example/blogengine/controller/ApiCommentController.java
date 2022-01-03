package com.example.blogengine.controller;

import com.example.blogengine.api.request.CommentRequest;
import com.example.blogengine.api.response.comment.CommentResponse;
import com.example.blogengine.exception.CommentNotFoundException;
import com.example.blogengine.exception.PostNotFoundException;
import com.example.blogengine.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class ApiCommentController {
    private final CommentService commentService;

    @PostMapping("")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<CommentResponse> editComment(@RequestBody CommentRequest commentRequest,
                                                       Principal principal) throws PostNotFoundException, CommentNotFoundException {
        return commentService.editComment(commentRequest, principal);
    }
}
