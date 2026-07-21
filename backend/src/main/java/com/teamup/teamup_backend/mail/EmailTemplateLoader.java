package com.teamup.teamup_backend.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class EmailTemplateLoader {

    private static final String TEMPLATE_DIRECTORY = "classpath:mail/";

    private final ResourceLoader resourceLoader;

    public String loadTemplate(String templateName) {
        Resource resource = resourceLoader.getResource(TEMPLATE_DIRECTORY + templateName);

        if (!resource.exists()) {
            throw new IllegalArgumentException(
                    "Email template not found: " + templateName
            );
        }

        try (InputStream inputStream = resource.getInputStream()) {
            return new String(
                    inputStream.readAllBytes(),
                    StandardCharsets.UTF_8
            );
        } catch (IOException exception) {
            throw new RuntimeException(
                    "Failed to load email template: " + templateName,
                    exception
            );
        }
    }
}