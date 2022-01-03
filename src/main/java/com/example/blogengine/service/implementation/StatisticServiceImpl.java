package com.example.blogengine.service.implementation;

import com.example.blogengine.api.response.settings.StatisticResponse;
import com.example.blogengine.model.User;
import com.example.blogengine.repository.PostRepository;
import com.example.blogengine.repository.PostVotesRepository;
import com.example.blogengine.repository.UserRepository;
import com.example.blogengine.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostVotesRepository postVotesRepository;

    public StatisticResponse getMyStatistic(Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        int SECOND = 1000;
        return new StatisticResponse()
                .setViewsCount(postRepository.findViewsCountByUser(user.getId()).orElse(0))
                .setPostsCount(postRepository.findPostCountByUser(user.getId()).orElse(0))
                .setLikesCount(postVotesRepository.findLikesByUser(user.getId()).orElse(0))
                .setDislikesCount(postVotesRepository.findDislikesByUser(user.getId()).orElse(0))
                .setFirstPublication(postRepository.findLatestPostByUser(user.getId()).orElse(new Date(0)).getTime()/ SECOND);
    }
}
