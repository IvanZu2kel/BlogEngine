package com.example.blogengine.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "mailjet.mail")
public class MailjetRegistrationParam {
    private String key;
    private String secret;
    private String from;
}
