package com.example.blogengine.service;

import com.example.blogengine.api.response.CalendarResponse;
import org.springframework.stereotype.Service;

@Service
public interface CalendarService {
    CalendarResponse getCalendar(String year);
}
