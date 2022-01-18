package com.example.blogengine.service.implementation;

import com.example.blogengine.api.request.SettingsRequest;
import com.example.blogengine.api.response.settings.SettingsResponse;
import com.example.blogengine.model.GlobalSettings;
import com.example.blogengine.repository.GlobalSettingsRepository;
import com.example.blogengine.service.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SettingsServiceImpl implements SettingsService {
    private final GlobalSettingsRepository globalSettingsRepository;

    @Override
    public SettingsResponse getGlobalSettings() {
        SettingsResponse settingsResponse = new SettingsResponse();
        settingsResponse.setMultiuserMode(globalSettingsRepository.findAllGlobalSettings("MULTIUSER_MODE").getValue().equals("YES"));
        settingsResponse.setPostPremoderation(globalSettingsRepository.findAllGlobalSettings("POST_PREMODERATION").getValue().equals("YES"));
        settingsResponse.setStatisticIsPublic(globalSettingsRepository.findAllGlobalSettings("STATISTICS_IS_PUBLIC").getValue().equals("YES"));
        return settingsResponse;
    }

    @Override
    public void putGlobalSettings(SettingsRequest settingsRequest) {
        String MULTIUSER_MODE = settingsRequest.isMultiuserMode() ? "YES" : "NO";
        String POST_PREMODERATION = settingsRequest.isPostPremoderation() ? "YES" : "NO";
        String STATISTICS_IS_PUBLIC = settingsRequest.isStatisticsIsPublic() ? "YES" : "NO";

        globalSettingsRepository.insertSettings("MULTIUSER_MODE", MULTIUSER_MODE);
        globalSettingsRepository.insertSettings("POST_PREMODERATION", POST_PREMODERATION);
        globalSettingsRepository.insertSettings("STATISTICS_IS_PUBLIC", STATISTICS_IS_PUBLIC);
    }
}
