package com.example.blogengine.service.implementation;

import com.example.blogengine.api.response.SettingsResponse;
import com.example.blogengine.repository.GlobalSettingsRepository;
import com.example.blogengine.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingsServiceImpl implements SettingsService {

    private final GlobalSettingsRepository globalSettingsRepository;

    @Autowired
    public SettingsServiceImpl(GlobalSettingsRepository globalSettingsRepository) {
        this.globalSettingsRepository = globalSettingsRepository;
    }

    @Override
    public SettingsResponse getGlobalSettings(){
        SettingsResponse settingsResponse = new SettingsResponse();
        settingsResponse.setMultiuserMode(globalSettingsRepository.findAllGlobalSettings("MULTIUSER_MODE").getValue().equals("YES"));
        settingsResponse.setPostPremoderation(globalSettingsRepository.findAllGlobalSettings("POST_PREMODERATION").getValue().equals("YES"));
        settingsResponse.setStatisticIsPublic(globalSettingsRepository.findAllGlobalSettings("STATISTICS_IS_PUBLIC").getValue().equals("YES"));

        return settingsResponse;
    }
}
