package com.example.blogengine.service.implementation;

import com.example.blogengine.api.request.PostVoteRequest;
import com.example.blogengine.api.response.ResultResponse;
import com.example.blogengine.exception.UsernameNotFoundException;
import com.example.blogengine.model.PostVotes;
import com.example.blogengine.model.User;
import com.example.blogengine.repository.PostRepository;
import com.example.blogengine.repository.PostVotesRepository;
import com.example.blogengine.repository.UserRepository;
import com.example.blogengine.service.PostVotesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PostVotesServiceImpl implements PostVotesService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostVotesRepository postVotesRepository;

    public ResultResponse postLike(PostVoteRequest postVoteRequest, Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow();
        if (postVotesRepository.findVotes(postVoteRequest.getPostId(), user.getId()) == null) {
            PostVotes postVotes = new PostVotes()
                    .setPost(postRepository.findPostById(postVoteRequest.getPostId()).orElseThrow())
                    .setUser(user)
                    .setTime(new Date())
                    .setValue((byte) 1);
            postVotesRepository.save(postVotes);
            return new ResultResponse().setResult(true);
        }
        PostVotes votesOld = postVotesRepository.findVotes(postVoteRequest.getPostId(), user.getId());
        if (votesOld.getValue() == -1) {
            votesOld
                    .setTime(new Date())
                    .setValue((byte) 1);
            postVotesRepository.save(votesOld);
        }
        else if (votesOld.getValue() == 1){
            return new ResultResponse().setResult(false);
        }
        return new ResultResponse().setResult(true);
    }

    public ResultResponse postDislike(PostVoteRequest postVoteRequest, Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow();
        if (postVotesRepository.findVotes(postVoteRequest.getPostId(), user.getId()) == null) {
            PostVotes postVotes = new PostVotes()
                    .setPost(postRepository.findPostById(postVoteRequest.getPostId()).orElseThrow())
                    .setUser(user)
                    .setTime(new Date())
                    .setValue((byte) -1);
            postVotesRepository.save(postVotes);
            return new ResultResponse().setResult(true);
        }
        PostVotes votesOld = postVotesRepository.findVotes(postVoteRequest.getPostId(), user.getId());
        if (votesOld.getValue() == 1) {
            votesOld
                    .setTime(new Date())
                    .setValue((byte) -1);
            postVotesRepository.save(votesOld);
        }
        else if (votesOld.getValue() == -1) {
            return new ResultResponse().setResult(false);
        }
        return new ResultResponse().setResult(true);
    }
}
