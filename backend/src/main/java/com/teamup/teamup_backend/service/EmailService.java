package com.teamup.teamup_backend.service;

public interface EmailService {

    void sendVerificationEmail(
            String to,
            String userName,
            String otp,
            int expiryMinutes
    );

}
