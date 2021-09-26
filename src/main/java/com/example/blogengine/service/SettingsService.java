package com.example.blogengine.service;

import com.example.blogengine.api.response.SettingsResponse;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {

    public SettingsResponse getGlobalSettings(){
        SettingsResponse settingsResponse = new SettingsResponse();
        settingsResponse.setPostPremoderation(true);
        settingsResponse.setStatisticIsPublic(true);
        return settingsResponse;
    }

}
