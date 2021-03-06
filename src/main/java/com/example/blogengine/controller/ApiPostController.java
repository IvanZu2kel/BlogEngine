package com.example.blogengine.controller;

import com.example.blogengine.api.request.PostRequest;
import com.example.blogengine.api.request.PostVoteRequest;
import com.example.blogengine.api.response.posts.PostResponse;
import com.example.blogengine.api.response.posts.PostsResponse;
import com.example.blogengine.api.response.ResultResponse;
import com.example.blogengine.exception.AuthorAndUserNoEqualsException;
import com.example.blogengine.exception.PostNotFoundException;
import com.example.blogengine.exception.StatusNotFoundException;
import com.example.blogengine.exception.UsernameNotFoundException;
import com.example.blogengine.model.PostVotes;
import com.example.blogengine.service.PostService;
import com.example.blogengine.service.PostVotesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class ApiPostController {
    private final PostService postService;
    private final PostVotesService postVotesService;

    @GetMapping("")
    public ResponseEntity<PostsResponse> getPosts(@RequestParam(defaultValue = "0") int offset,
                                                  @RequestParam(defaultValue = "10") int limit,
                                                  @RequestParam(defaultValue = "") String mode) {
        return new ResponseEntity<>(postService.getPosts(offset, limit, mode), HttpStatus.OK);
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<ResultResponse> postPost(@RequestBody PostRequest postRequest,
                                                   Principal principal) {
        return new ResponseEntity<>(postService.createPost(postRequest, principal), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<PostsResponse> getPostsByQuery(@RequestParam(required = false, defaultValue = "0") int offset,
                                                         @RequestParam(required = false, defaultValue = "10") int limit,
                                                         @RequestParam(required = false, defaultValue = " ") String query) {
        return new ResponseEntity<>(postService.getPostsSearch(offset, limit, query), HttpStatus.OK);
    }

    @GetMapping("/byDate")
    public ResponseEntity<PostsResponse> getPostsByDate(@RequestParam(required = false, defaultValue = "0") int offset,
                                                        @RequestParam(required = false, defaultValue = "10") int limit,
                                                        @RequestParam(required = false, defaultValue = "") String date) {
        return new ResponseEntity<>(postService.getPostsByDate(offset, limit, date), HttpStatus.OK);
    }

    @GetMapping("/byTag")
    public ResponseEntity<PostsResponse> getPostsByTag(@RequestParam(required = false, defaultValue = "0") int offset,
                                                       @RequestParam(required = false, defaultValue = "10") int limit,
                                                       @RequestParam(required = false, defaultValue = "") String tag) {
        return new ResponseEntity<>(postService.getPostsByTag(offset, limit, tag), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostsById(@PathVariable int id,
                                                     Principal principal) throws UsernameNotFoundException, PostNotFoundException {
        return new ResponseEntity<>(postService.getPostsById(id, principal), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<ResultResponse> putPostsById(@PathVariable int id,
                                                     @RequestBody PostRequest postRequest,
                                                     Principal principal) throws UsernameNotFoundException, PostNotFoundException, AuthorAndUserNoEqualsException {
        return new ResponseEntity<>(postService.putPostsById(id, postRequest, principal), HttpStatus.OK);
    }

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<PostsResponse> getPostsMy(@RequestParam(required = false, defaultValue = "0") int offset,
                                                    @RequestParam(required = false, defaultValue = "10") int limit,
                                                    @RequestParam(required = false, defaultValue = "") String status,
                                                    Principal principal) throws StatusNotFoundException {
        return new ResponseEntity<>(postService.getPostsMy(offset, limit, status, principal), HttpStatus.OK);
    }

    @GetMapping("/moderation")
    @PreAuthorize("hasAuthority('user:moderate')")
    public ResponseEntity<PostsResponse> getModeratePost(@RequestParam(required = false, defaultValue = "0") int offset,
                                                    @RequestParam(required = false, defaultValue = "10") int limit,
                                                    @RequestParam(required = false, defaultValue = "") String status,
                                                    Principal principal) throws StatusNotFoundException {
        return new ResponseEntity<>(postService.getModeratePost(offset, limit, status, principal), HttpStatus.OK);
    }

    @PostMapping("/like")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<ResultResponse> postLike(@RequestBody PostVoteRequest postVoteRequest,
                                                   Principal principal) {
        return new ResponseEntity<>(postVotesService.postLike(postVoteRequest, principal), HttpStatus.OK);
    }

    @PostMapping("/dislike")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<ResultResponse> postDislike(@RequestBody PostVoteRequest postVoteRequest,
                                                      Principal principal) {
        return new ResponseEntity<>(postVotesService.postDislike(postVoteRequest, principal), HttpStatus.OK);
    }
}
