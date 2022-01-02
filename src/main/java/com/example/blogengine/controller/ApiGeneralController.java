package com.example.blogengine.controller;

import com.example.blogengine.api.response.CalendarResponse;
import com.example.blogengine.api.response.InitResponse;
import com.example.blogengine.api.response.SettingsResponse;
import com.example.blogengine.api.response.TagsResponse;
import com.example.blogengine.service.CalendarService;
import com.example.blogengine.service.SettingsService;
import com.example.blogengine.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiGeneralController {
    private final SettingsService settingsService;
    private final InitResponse initResponse;
    private final TagService tagService;
    private final CalendarService calendarService;

    @GetMapping("/init")
    private InitResponse init(){
        return initResponse;
    }

    @GetMapping("/settings")
    private SettingsResponse settings() {
        return settingsService.getGlobalSettings();
    }

    @GetMapping("/tag")
    private TagsResponse getTags(@RequestParam(required = false, defaultValue = "") String query){
        return tagService.getTags(query);
    }

    @GetMapping("/calendar")
    public CalendarResponse getCalendar(
            @RequestParam(required = false, defaultValue = "none") String year) {
        return calendarService.getCalendar(year  );
    }
}
