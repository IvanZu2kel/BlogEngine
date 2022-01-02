package com.example.blogengine.service;

import com.example.blogengine.api.response.SettingsResponse;
import org.springframework.stereotype.Service;

@Service
public interface SettingsService {
    SettingsResponse getGlobalSettings();
}
