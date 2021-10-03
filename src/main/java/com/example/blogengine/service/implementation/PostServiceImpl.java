package com.example.blogengine.service.implementation;

import com.example.blogengine.api.response.CommentResponse;
import com.example.blogengine.api.response.PostResponse;
import com.example.blogengine.api.response.PostResponseList;
import com.example.blogengine.api.response.PostsResponse;
import com.example.blogengine.exception.UsernameNotFoundException;
import com.example.blogengine.model.Post;
import com.example.blogengine.model.PostComment;
import com.example.blogengine.model.User;
import com.example.blogengine.repository.CommentRepository;
import com.example.blogengine.repository.PostRepository;
import com.example.blogengine.repository.TagRepository;
import com.example.blogengine.repository.UserRepository;
import com.example.blogengine.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    public PostServiceImpl(PostRepository postRepository,
                           CommentRepository commentRepository, TagRepository tagRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
    }

    @Override
    public PostsResponse getPosts(int offset, int limit, String mode) {

        Pageable pageable = PageRequest.of(offset / limit, limit);

        Page<Post> postPage;

        switch (mode) {
            case "popular":
                postPage = postRepository.findAllPostsByCommentsDesc(pageable);
                break;
            case "best":
                postPage = postRepository.findAllPostsByVotesDesc(pageable);
                break;
            case "early":
                postPage = postRepository.findAllPostsByTime(pageable);
                break;
            default:
                postPage = postRepository.findAllPostsByTimeDesc(pageable);
                break;
        }

        return createPostResponse(postPage, postRepository.findAllPosts().size());
    }

    @Override
    public ResponseEntity<?> getPostsSearch(int offset, int limit, String query) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Post> pageOfTags = postRepository.findAllPostsBySearch(query, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(createPostResponse(pageOfTags, (int) pageOfTags.getTotalElements()));
    }

    @Override
    public ResponseEntity<?> getPostsByDate(int offset, int limit, String date) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Post> postPage = postRepository.findAllPostsByDate(date, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(createPostResponse(postPage, (int) postPage.getTotalElements()));
    }

    @Override
    public ResponseEntity<?> getPostsByTag(int offset, int limit, String tag) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Post> postPage = postRepository.findAllPostsByTag(tag, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(createPostResponse(postPage, (int) postPage.getTotalElements()));
    }

    @Override
    public ResponseEntity<?> getPostsById(int id, Principal principal) {
        List<PostComment> commentsList = commentRepository.findPostCommentsById(id);
        List<String> tagList = tagRepository.findTagsById(id);
        List<CommentResponse> commentResponseList = new ArrayList<>();

        for (PostComment c : commentsList) {
            commentResponseList.add(new CommentResponse(c));
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
