package com.example.blogengine.controller;

import com.example.blogengine.api.request.ModeratorRequest;
import com.example.blogengine.api.request.SettingsRequest;
import com.example.blogengine.api.response.ResultResponse;
import com.example.blogengine.api.response.security.InitResponse;
import com.example.blogengine.api.response.settings.CalendarResponse;
import com.example.blogengine.api.response.settings.SettingsResponse;
import com.example.blogengine.api.response.tags.TagsResponse;
import com.example.blogengine.exception.IncorrectFormatException;
import com.example.blogengine.exception.ModeratorNotFoundException;
import com.example.blogengine.exception.PostNotFoundException;
import com.example.blogengine.exception.StatusNotFoundException;
import com.example.blogengine.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiGeneralController {
    private final SettingsService settingsService;
    private final InitResponse initResponse;
    private final TagService tagService;
    private final CalendarService calendarService;
    private final StorageService storageService;
    private final PostService postService;

    @GetMapping("/init")
    public InitResponse init(){
        return initResponse;
    }

    @GetMapping("/settings")
    public SettingsResponse settings() {
        return settingsService.getGlobalSettings();
    }

    @GetMapping("/tag")
    public TagsResponse getTags(@RequestParam(required = false, defaultValue = "") String query){
        return tagService.getTags(query);
    }

    @GetMapping("/calendar")
    public CalendarResponse getCalendar(
            @RequestParam(required = false, defaultValue = "none") String year) {
        return calendarService.getCalendar(year  );
    }

    @PostMapping("/image")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<Object> fileUpload(@RequestParam("image") MultipartFile file) throws IncorrectFormatException, IOException {
        return new ResponseEntity<>(storageService.store(file), HttpStatus.OK);
    }

    @PostMapping("/moderation")
    @PreAuthorize("hasAuthority('user:moderate')")
    public ResponseEntity<ResultResponse> postModeratePost(@RequestBody ModeratorRequest moderatorRequest,
                                                           Principal principal) throws StatusNotFoundException, PostNotFoundException, ModeratorNotFoundException {
        return new ResponseEntity<>(postService.postModeratePost(moderatorRequest, principal), HttpStatus.OK);
    }

    @PutMapping("/settings")
    @PreAuthorize("hasAuthority('user:moderate')")
    public void putSettings(@RequestBody SettingsRequest settingsRequest) {
        settingsService.putGlobalSettings(settingsRequest);
    }

    @Bean
    public MultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }
}
