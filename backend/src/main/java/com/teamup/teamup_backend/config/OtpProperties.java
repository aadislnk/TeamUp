package com.teamup.teamup_backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "app.otp")
@Component
@Getter
@Setter
public class OtpProperties {

    private int length;
    private int expiryMinutes;
    private int resendCooldownSeconds;
    private int maxResendAttempts;
    private int maxVerificationAttempts;

}
