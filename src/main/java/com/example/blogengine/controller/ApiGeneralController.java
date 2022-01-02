package com.example.blogengine.controller;

import com.example.blogengine.api.response.CalenderResponse;
import com.example.blogengine.api.response.InitResponse;
import com.example.blogengine.api.response.SettingsResponse;
import com.example.blogengine.api.response.TagsResponse;
import com.example.blogengine.service.CalendarService;
import com.example.blogengine.service.SettingsService;
import com.example.blogengine.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ApiGeneralController {
    private final SettingsService settingsService;
    private final InitResponse initResponse;
    private final TagService tagService;
    private final CalendarService calendarService;

    @GetMapping("/api/init")
    private InitResponse init(){
        return initResponse;
    }

    @GetMapping("/api/settings")
    private SettingsResponse settings() {
        return settingsService.getGlobalSettings();
    }

    @GetMapping("/api/tag")
    private TagsResponse getTags(@RequestParam(required = false, defaultValue = "") String query){
        return tagService.getTags(query);
    }

    @GetMapping("/api/calendar")
    public CalenderResponse getCalendar(
            @RequestParam(required = false, defaultValue = "none") String year) {
        return calendarService.getCalendar(year  );
    }
}
