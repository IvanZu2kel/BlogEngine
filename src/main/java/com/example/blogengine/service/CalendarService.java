package com.example.blogengine.service;

import com.example.blogengine.api.response.CalenderResponse;

public interface CalendarService {
    CalenderResponse getCalendar(String year);
}
