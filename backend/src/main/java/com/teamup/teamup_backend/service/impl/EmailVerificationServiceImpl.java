package com.teamup.teamup_backend.service.impl;

import com.teamup.teamup_backend.config.OtpProperties;
import com.teamup.teamup_backend.entity.User;
import com.teamup.teamup_backend.exception.UserNotFoundException;
import com.teamup.teamup_backend.repository.UserRepository;
import com.teamup.teamup_backend.service.EmailVerificationService;
import com.teamup.teamup_backend.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailVerificationServiceImpl
        implements EmailVerificationService {

    private final UserRepository userRepository;
    private final OtpService otpService;
    private final OtpProperties otpProperties;

    @Override
    public void sendVerificationOtp(String email) {

        User user = getUser(email);

        if (Boolean.TRUE.equals(user.getEmailVerified())) {
            return;
        }

        otpService.sendVerificationOtp(email);
    }

    @Override
    public void verifyEmail(String email, String otp) {

        User user = getUser(email);

        if (Boolean.TRUE.equals(user.getEmailVerified())) {
            return;
        }

        otpService.verifyOtp(email, otp);
    }

    @Override
    public void resendVerificationOtp(String email) {

        User user = getUser(email);

        if (Boolean.TRUE.equals(user.getEmailVerified())) {
            return;
        }

        // We'll improve resend rules later if needed.
        otpService.sendVerificationOtp(email);
    }

    private User getUser(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));
    }

}