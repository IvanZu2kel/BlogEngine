package com.example.blogengine.service;

import com.example.blogengine.api.response.settings.StatisticResponse;
import com.example.blogengine.exception.ModeratorNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.security.Principal;

@Service
public interface StatisticService {
    StatisticResponse getMyStatistic(Principal principal);

    StatisticResponse getAllStatistic(Principal principal) throws UserPrincipalNotFoundException, ModeratorNotFoundException;
}
