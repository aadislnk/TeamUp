package com.teamup.teamup_backend.service;

import com.teamup.teamup_backend.entity.User;

public interface OtpService {

    void sendVerificationOtp(String email);

    void verifyOtp(String email, String otp);

    void cleanupExpiredTokens();

}