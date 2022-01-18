package com.example.blogengine.service.implementation;

import com.example.blogengine.api.request.CommentRequest;
import com.example.blogengine.api.response.ErrorResponse;
import com.example.blogengine.api.response.comment.CommentResponse;
import com.example.blogengine.exception.CommentNotFoundException;
import com.example.blogengine.exception.PostNotFoundException;
import com.example.blogengine.model.PostComment;
import com.example.blogengine.model.User;
import com.example.blogengine.repository.PostCommentRepository;
import com.example.blogengine.repository.PostRepository;
import com.example.blogengine.repository.UserRepository;
import com.example.blogengine.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final PostCommentRepository postCommentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<CommentResponse> editComment(CommentRequest commentRequest, Principal principal) throws PostNotFoundException, CommentNotFoundException {
        CommentResponse commentResponse = new CommentResponse();
        if (commentRequest.getText().length() <= 3) {
            return new ResponseEntity<>(new CommentResponse()
                    .setResult(false)
                    .setErrors(new ErrorResponse().setText("Текст комментария не задан или слишком короткий")),
                    HttpStatus.BAD_REQUEST);
        } else {
            User user = userRepository.findByEmail(principal.getName()).orElseThrow();
            PostComment postComment = new PostComment()
                    .setPost(postRepository.findPostById(commentRequest.getPostId())
                            .orElseThrow(() -> new PostNotFoundException("поста с данным айди не существует")));
            if (commentRequest.getParentId() != null) {
                postComment.setParent(postCommentRepository.findById(commentRequest.getParentId())
                        .orElseThrow(() -> new CommentNotFoundException("комментария с данным айди не существует")));
            }
            postComment
                    .setUser(user)
                    .setTime(new Date())
                    .setText(commentRequest.getText());
            PostComment comment = postCommentRepository.save(postComment);
            commentResponse.setId(comment.getId());
            return new ResponseEntity<>(commentResponse, HttpStatus.OK);
        }
    }
}
