package com.teamup.teamup_backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "app.mail")
@Component
@Getter
@Setter
public class MailProperties {

    private String fromAddress;
    private String fromName;

}
