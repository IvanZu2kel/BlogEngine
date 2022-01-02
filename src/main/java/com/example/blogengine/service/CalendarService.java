package com.example.blogengine.service;

import com.example.blogengine.api.response.CalenderResponse;
import org.springframework.stereotype.Service;

@Service
public interface CalendarService {
    CalenderResponse getCalendar(String year);
}
