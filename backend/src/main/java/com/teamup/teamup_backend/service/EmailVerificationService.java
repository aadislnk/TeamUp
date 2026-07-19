package com.teamup.teamup_backend.service;

public interface EmailVerificationService {

    void sendVerificationOtp(String email);

    void verifyEmail(String email, String otp);

    void resendVerificationOtp(String email);

}
