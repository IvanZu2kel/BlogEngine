package com.example.blogengine.service.implementation;

import com.example.blogengine.api.request.PostVoteRequest;
import com.example.blogengine.api.response.ResultResponse;
import com.example.blogengine.model.PostVotes;
import com.example.blogengine.model.User;
import com.example.blogengine.repository.PostRepository;
import com.example.blogengine.repository.PostVotesRepository;
import com.example.blogengine.repository.UserRepository;
import com.example.blogengine.service.PostVotesService;
import lombok.RequiredArgsConstructor;
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
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        Optional<PostVotes> postVotesOpt = postVotesRepository.findByPostId(postVoteRequest.getPostId(), user.getId());
        ResultResponse resultResponse = new ResultResponse();
        if (postVotesOpt.isEmpty()) {
            PostVotes postVotes = new PostVotes()
                    .setPost(postRepository.findPostById(postVoteRequest.getPostId()).orElseThrow())
                    .setUser(user)
                    .setTime(new Date())
                    .setValue((byte) 1);
            postVotesRepository.save(postVotes);
            resultResponse.setResult(true);
        } else {
            PostVotes postVotes = postVotesOpt.get();
            if (postVotes.getValue() == (byte) 0) {
                postVotes.setTime(new Date());
                postVotes.setValue((byte) 1);
                resultResponse.setResult(true);
            } else if (postVotes.getValue() == (byte) 1){
                postVotes.setTime(new Date());
                postVotes.setValue((byte)-1);
                resultResponse.setResult(false);
            } else {
                postVotes.setTime(new Date());
                postVotes.setValue((byte) 1);
                resultResponse.setResult(true);
            }
            postVotesRepository.save(postVotes);
        }
        return resultResponse;
    }

    public ResultResponse postDislike(PostVoteRequest postVoteRequest, Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        Optional<PostVotes> postVotesOpt = postVotesRepository.findByPostId(postVoteRequest.getPostId(), user.getId());
        ResultResponse resultResponse = new ResultResponse();
        if (postVotesOpt.isEmpty()) {
            PostVotes postVotes = new PostVotes()
                    .setPost(postRepository.findPostById(postVoteRequest.getPostId()).orElseThrow())
                    .setUser(user)
                    .setTime(new Date())
                    .setValue((byte) 0);
            postVotesRepository.save(postVotes);
            resultResponse.setResult(true);
        } else {
            PostVotes postVotes = postVotesOpt.get();
            if (postVotes.getValue() == (byte) 1) {
                postVotes.setTime(new Date());
                postVotes.setValue((byte) 0);
                resultResponse.setResult(true);
            } else if (postVotes.getValue() == (byte) 0){
                postVotes.setTime(new Date());
                postVotes.setValue((byte)-1);
                resultResponse.setResult(false);
            } else {
                postVotes.setTime(new Date());
                postVotes.setValue((byte) 0);
                resultResponse.setResult(true);
            }
            postVotesRepository.save(postVotes);
        }
        return resultResponse;
    }
}
