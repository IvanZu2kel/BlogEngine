package com.example.blogengine.config.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class Url {
    @Value("${server.base_url}")
    private String baseUrl;
}
