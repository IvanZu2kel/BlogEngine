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

    private final int MULTIUSER_MODE = 1;
    private final int POST_PREMODERATION = 2;
    private final int STATISTICS_IS_PUBLIC = 3;

    public SettingsResponse getGlobalSettings() {
        SettingsResponse settingsResponse = new SettingsResponse();
        globalSettingsRepository.findAll().forEach(gS -> {
            switch (gS.getId()) {
                case MULTIUSER_MODE:
                    settingsResponse.setMultiuserMode(gS.getValue().equals("YES"));
                    break;
                case POST_PREMODERATION:
                    settingsResponse.setPostPremoderation(gS.getValue().equals("YES"));
                    break;
                case STATISTICS_IS_PUBLIC:
                    settingsResponse.setStatisticIsPublic(gS.getValue().equals("YES"));
                    break;
            }
        });
        return settingsResponse;
    }

    public void putGlobalSettings(SettingsRequest settingsRequest) {
        globalSettingsRepository.findAll().forEach(gS -> {
            switch (gS.getId()) {
                case MULTIUSER_MODE:
                    gS.setValue(settingsRequest.isMultiuserMode() ? "YES" : "NO");
                    globalSettingsRepository.save(gS);
                    break;
                case POST_PREMODERATION:
                    gS.setValue(settingsRequest.isPostPremoderation() ? "YES" : "NO");
                    globalSettingsRepository.save(gS);
                    break;

                case STATISTICS_IS_PUBLIC:
                    gS.setValue(settingsRequest.isStatisticsIsPublic() ? "YES" : "NO");
                    globalSettingsRepository.save(gS);
                    break;
            }
        });
    }
}
