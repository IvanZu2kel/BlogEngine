package com.example.blogengine.controller;

import com.example.blogengine.api.InitResponse;
import com.example.blogengine.api.SettingsResponse;
import com.example.blogengine.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class ApiGeneralController {

    private final SettingsService settingsService;
    private final InitResponse initResponse;

    @Autowired
    public ApiGeneralController(SettingsService settingsService, InitResponse initResponse) {
        this.settingsService = settingsService;
        this.initResponse = initResponse;
    }

    @GetMapping("/api/init")
    private InitResponse init(InitResponse initResponse){
        return initResponse;
    }

    @GetMapping("/api/settings")
    private ResponseEntity<SettingsResponse> settings() {
        return new ResponseEntity<>(settingsService.getGlobalSettings(), HttpStatus.OK);
    }
}
