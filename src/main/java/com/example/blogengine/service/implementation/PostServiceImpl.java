package com.example.blogengine.service.implementation;

import com.example.blogengine.api.response.PostResponseList;
import com.example.blogengine.api.response.PostsResponse;
import com.example.blogengine.model.Post;
import com.example.blogengine.repository.PostRepository;
import com.example.blogengine.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    public PostsResponse getPosts(int offset, int limit, String mode){

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

    @Override
    public ResponseEntity<?> getPostsSearch(int offset, int limit, String query) {
        Pageable pageable = PageRequest.of(offset/limit, limit);
        Page<Post> pageOfTags = postRepository.findAllPostsBySearch(query, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(createPostResponse(pageOfTags, (int) pageOfTags.getTotalElements()));
    }
}
