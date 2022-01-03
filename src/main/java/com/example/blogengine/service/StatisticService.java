package com.example.blogengine.service;

import com.example.blogengine.api.response.settings.StatisticResponse;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public interface StatisticService {
    StatisticResponse getMyStatistic(Principal principal);
}
