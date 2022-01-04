package com.example.blogengine.service.implementation;

import com.example.blogengine.api.response.settings.StatisticResponse;
import com.example.blogengine.exception.ModeratorNotFoundException;
import com.example.blogengine.model.GlobalSettings;
import com.example.blogengine.model.User;
import com.example.blogengine.repository.GlobalSettingsRepository;
import com.example.blogengine.repository.PostRepository;
import com.example.blogengine.repository.PostVotesRepository;
import com.example.blogengine.repository.UserRepository;
import com.example.blogengine.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.security.Principal;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostVotesRepository postVotesRepository;
    private final GlobalSettingsRepository settingsRepository;

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

    public StatisticResponse getAllStatistic(Principal principal) throws UserPrincipalNotFoundException, ModeratorNotFoundException {
        if (principal == null) throw new UserPrincipalNotFoundException("пользователь не найден");
        if (settingsRepository.findByCode("STATISTICS_IS_PUBLIC").orElse(new GlobalSettings()).getValue().equals("NO")) {
            User user = userRepository.findByEmail(principal.getName()).orElseThrow();
            if (user.getIsModerator() == 0) {
                throw new ModeratorNotFoundException("пользователь не модератор");
            }
        }
        int SECOND = 1000;
        return new StatisticResponse()
                .setViewsCount(postRepository.findViewsCount().orElse(0))
                .setPostsCount(postRepository.findPostCount().orElse(0))
                .setLikesCount(postVotesRepository.findLikesCount().orElse(0))
                .setDislikesCount(postVotesRepository.findDislikeCount().orElse(0))
                .setFirstPublication(postRepository.findLatestPost().orElse(new Date(0)).getTime()/ SECOND);
    }
}
