package com.example.blogengine.service;

import com.example.blogengine.api.response.settings.CalendarResponse;
import org.springframework.stereotype.Service;

@Service
public interface CalendarService {
    CalendarResponse getCalendar(String year);
}
