package com.example.blogengine.service.implementation;

import com.example.blogengine.api.response.SettingsResponse;
import com.example.blogengine.model.GlobalSettings;
import com.example.blogengine.repository.GlobalSettingsRepository;
import com.example.blogengine.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SettingsServiceImpl implements SettingsService {

    private final GlobalSettingsRepository globalSettingsRepository;

    @Autowired
    public SettingsServiceImpl(GlobalSettingsRepository globalSettingsRepository) {
        this.globalSettingsRepository = globalSettingsRepository;
    }

    @Override
    public SettingsResponse getGlobalSettings() {
        SettingsResponse settingsResponse = new SettingsResponse();

        List<GlobalSettings> globalSettings = globalSettingsRepository.findAll();

        if (globalSettings.size() != 0) {
            Optional<GlobalSettings> multiuserMode = globalSettings.stream()
                    .filter(gs -> gs.getCode().equals("MULTIUSER_MODE"))
                    .findFirst();
            settingsResponse.setMultiuserMode(multiuserMode.get().getValue().equals("YES"));

            Optional<GlobalSettings> postPremoderation = globalSettings.stream()
                    .filter(gs -> gs.getCode().equals("POST_PREMODERATION"))
                    .findFirst();
            settingsResponse.setPostPremoderation(postPremoderation.get().getValue().equals("YES"));

            Optional<GlobalSettings> statisticsIsPublic = globalSettings.stream()
                    .filter(gs -> gs.getCode().equals("STATISTICS_IS_PUBLIC"))
                    .findFirst();
            settingsResponse.setStatisticIsPublic(statisticsIsPublic.get().getValue().equals("YES"));

        }
        return settingsResponse;
    }
}
