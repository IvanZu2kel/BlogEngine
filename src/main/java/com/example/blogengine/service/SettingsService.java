package com.example.blogengine.service;

import com.example.blogengine.api.request.SettingsRequest;
import com.example.blogengine.api.response.settings.SettingsResponse;
import org.springframework.stereotype.Service;

@Service
public interface SettingsService {
    SettingsResponse getGlobalSettings();

    void putGlobalSettings(SettingsRequest settingsRequest);
}
