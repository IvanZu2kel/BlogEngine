package com.example.blogengine.service;

import com.example.blogengine.api.request.PostVoteRequest;
import com.example.blogengine.api.response.ResultResponse;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public interface PostVotesService {
    ResultResponse postLike(PostVoteRequest postVoteRequest, Principal principal);

    ResultResponse postDislike(PostVoteRequest postVoteRequest, Principal principal);
}
