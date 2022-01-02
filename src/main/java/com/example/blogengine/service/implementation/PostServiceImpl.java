package com.example.blogengine.service.implementation;

import com.example.blogengine.api.response.*;
import com.example.blogengine.exception.UsernameNotFoundException;
import com.example.blogengine.model.Post;
import com.example.blogengine.model.PostComment;
import com.example.blogengine.model.User;
import com.example.blogengine.repository.CommentRepository;
import com.example.blogengine.repository.PostRepository;
import com.example.blogengine.repository.TagRepository;
import com.example.blogengine.repository.UserRepository;
import com.example.blogengine.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    public PostsResponse getPosts(int offset, int limit, String mode) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Post> postPage;
        switch (mode) {
            case "popular" -> postPage = postRepository.findAllPostsByCommentsDesc(pageable);
            case "best" -> postPage = postRepository.findAllPostsByVotesDesc(pageable);
            case "early" -> postPage = postRepository.findAllPostsByTime(pageable);
            default -> postPage = postRepository.findAllPostsByTimeDesc(pageable);
        }
        return createPostResponse(postPage, postRepository.findAllPosts().size());
    }

    public ResponseEntity<?> getPostsSearch(int offset, int limit, String query) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Post> pageOfTags = postRepository.findAllPostsBySearch(query, pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(createPostResponse(pageOfTags, (int) pageOfTags.getTotalElements()));
    }

    public ResponseEntity<?> getPostsByDate(int offset, int limit, String date) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Post> postPage = postRepository.findAllPostsByDate(date, pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(createPostResponse(postPage, (int) postPage.getTotalElements()));
    }

    public ResponseEntity<?> getPostsByTag(int offset, int limit, String tag) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Post> postPage = postRepository.findAllPostsByTag(tag, pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(createPostResponse(postPage, (int) postPage.getTotalElements()));
    }

    public ResponseEntity<?> getPostsById(int id, Principal principal) {
        List<PostComment> commentsList = commentRepository.findPostCommentsById(id);
        List<String> tagList = tagRepository.findTagsById(id);
        List<CommentResponse> commentResponseList = new ArrayList<>();
        for (PostComment c : commentsList) {
            commentResponseList.add(new CommentResponse()
                    .setId(c.getId())
                    .setTimestamp(c.getTime().getTime() / 1000)
                    .setText(c.getText())
                    .setUser(new UserPostResponse().setId(c.getUser().getId()).setName(c.getUser().getName()))
            );
        }
        Post post;
        if (!(principal == null)) {
            post = postRepository.findPostById(id);
            if (post == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            User user = null;
            try {
                user = userRepository.findByEmail(principal.getName())
                        .orElseThrow(() -> new UsernameNotFoundException("user not found"));
            } catch (UsernameNotFoundException e) {
                e.printStackTrace();
            }
            if (!(post.getUser().getId() == user.getId()) || user.getIsModerator() == 0) {
                post.setViewCount(post.getViewCount() + 1);
                postRepository.save(post);
            }
        } else {
            post = postRepository.findPostAcceptedById(id);
            if (post == null) {
                return null;
            }
            post.setViewCount(post.getViewCount() + 1);
            postRepository.save(post);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new PostResponse(commentResponseList, post, tagList));
    }

    public ResponseEntity<?> getPostsMy(int offset, int limit, String status, Principal principal) {
        Pageable pageable;
        pageable = PageRequest.of(offset / limit, limit);

        switch (status) {
            case "inactive" -> {
                Page<Post> pageMy = postRepository.findPostsMyInactive(pageable, principal.getName());
                return ResponseEntity.ok(createPostResponse(pageMy, (int) pageMy.getTotalElements()));
            }
            case "pending" -> {
                Page<Post> pageMy = postRepository.findPostsMyIsActive("NEW", principal.getName(), pageable);
                return ResponseEntity.ok(createPostResponse(pageMy, (int) pageMy.getTotalElements()));
            }
            case "declined" -> {
                Page<Post> pageMy = postRepository.findPostsMyIsActive("DECLINED", principal.getName(), pageable);
                return ResponseEntity.ok(createPostResponse(pageMy, (int) pageMy.getTotalElements()));
            }
            case "published" -> {
                Page<Post> pageMy = postRepository.findPostsMyIsActive("ACCEPTED", principal.getName(), pageable);
                return ResponseEntity.ok(createPostResponse(pageMy, (int) pageMy.getTotalElements()));
            }
        }
        return null;
    }

    private PostsResponse createPostResponse(Page<Post> pageTags, int size) {
        List<PostResponseList> postResponseList = new ArrayList<>();
        for (Post p : pageTags) {
            postResponseList.add(new PostResponseList(p));
        }
        PostsResponse postsResponse = new PostsResponse();
        postsResponse.setPosts(postResponseList);
        postsResponse.setCount(size);
        return postsResponse;
    }
}
