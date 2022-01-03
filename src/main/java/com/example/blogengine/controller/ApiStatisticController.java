package com.example.blogengine.controller;

import com.example.blogengine.api.response.settings.StatisticResponse;
import com.example.blogengine.exception.ModeratorNotFoundException;
import com.example.blogengine.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.security.Principal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiStatisticController {
    private final StatisticService statisticService;

    @GetMapping("/statistics/my")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<StatisticResponse> myStatistics(Principal principal) {
        return new ResponseEntity<>(statisticService.getMyStatistic(principal), HttpStatus.OK);
    }

    @GetMapping("/statistics/all")
    public ResponseEntity<StatisticResponse> allStatistics(Principal principal) throws UserPrincipalNotFoundException, ModeratorNotFoundException {
        return new ResponseEntity<>(statisticService.getAllStatistic(principal), HttpStatus.OK);
    }
}
