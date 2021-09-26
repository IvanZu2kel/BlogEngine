package com.example.blogengine.controller;

import com.example.blogengine.api.response.InitResponse;
import com.example.blogengine.api.response.SettingsResponse;
import com.example.blogengine.api.response.TagsResponse;
import com.example.blogengine.service.SettingsService;
import com.example.blogengine.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class ApiGeneralController {

    private final SettingsService settingsService;
    private final InitResponse initResponse;
    private final TagService tagService;

    public ApiGeneralController(SettingsService settingsService, InitResponse initResponse, TagService tagService) {
        this.settingsService = settingsService;
        this.initResponse = initResponse;
        this.tagService = tagService;
    }


    @GetMapping("/api/init")
    private InitResponse init(InitResponse initResponse){
        return initResponse;
    }

    @GetMapping("/api/settings")
    private ResponseEntity<SettingsResponse> settings() {
        return new ResponseEntity<>(settingsService.getGlobalSettings(), HttpStatus.OK);
    }

    @GetMapping("/api/tag")
    private TagsResponse getTags(@RequestParam String query){
        return tagService.getTags(query);
    }
}
