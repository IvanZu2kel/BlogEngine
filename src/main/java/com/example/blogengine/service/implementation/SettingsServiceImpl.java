package com.example.blogengine.service.implementation;

import com.example.blogengine.api.response.SettingsResponse;
import com.example.blogengine.service.SettingsService;
import org.springframework.stereotype.Service;

@Service
public class SettingsServiceImpl implements SettingsService {

    @Override
    public SettingsResponse getGlobalSettings(){
        SettingsResponse settingsResponse = new SettingsResponse();
        settingsResponse.setPostPremoderation(true);
        settingsResponse.setStatisticIsPublic(true);

        return settingsResponse;
    }
}
