package com.teamup.teamup_backend.service.impl;

import com.teamup.teamup_backend.config.OtpProperties;
import com.teamup.teamup_backend.entity.EmailVerificationToken;
import com.teamup.teamup_backend.entity.User;
import com.teamup.teamup_backend.enums.OtpPurpose;
import com.teamup.teamup_backend.enums.TokenStatus;
import com.teamup.teamup_backend.exception.*;
import com.teamup.teamup_backend.repository.EmailVerificationTokenRepository;
import com.teamup.teamup_backend.repository.UserRepository;
import com.teamup.teamup_backend.service.EmailService;
import com.teamup.teamup_backend.service.OtpService;
import com.teamup.teamup_backend.util.OtpGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OtpServiceImpl implements OtpService {

    private final UserRepository userRepository;
    private final EmailVerificationTokenRepository tokenRepository;
    private final OtpGenerator otpGenerator;
    private final EmailService emailService;
    private final OtpProperties otpProperties;

    @Override
    public void sendVerificationOtp(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        EmailVerificationToken latestToken =
                tokenRepository.findTopByUserAndPurposeOrderByCreatedAtDesc(
                        user,
                        OtpPurpose.EMAIL_VERIFICATION
                ).orElse(null);

        validateResendRules(latestToken);

        invalidatePreviousToken(user);

        String otp = otpGenerator.generateOtp();

        EmailVerificationToken token =
                createVerificationToken(user, otp, latestToken);

        tokenRepository.save(token);

        emailService.sendVerificationEmail(
                user.getEmail(),
                user.getFullName(),
                otp,
                otpProperties.getExpiryMinutes()
        );
    }

    @Override
    public void verifyOtp(String email, String otp) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        EmailVerificationToken token = tokenRepository
                .findByUserAndPurposeAndStatus(
                        user,
                        OtpPurpose.EMAIL_VERIFICATION,
                        TokenStatus.ACTIVE
                )
                .orElseThrow(() -> new OtpNotFoundException("OTP not found"));

        if (!token.getOtp().equals(otp)) {

            incrementFailedAttempts(token);

            if (token.getFailedAttempts() >=
                    otpProperties.getMaxVerificationAttempts()) {

                token.setStatus(TokenStatus.REVOKED);

                tokenRepository.save(token);

                throw new TooManyVerificationAttemptsException(
                        "Maximum verification attempts exceeded."
                );
            }

            throw new InvalidOtpException("Invalid OTP");
        }

        markVerified(token);

        user.setEmailVerified(true);

        userRepository.save(user);
    }

    @Override
    public void cleanupExpiredTokens() {

        tokenRepository.deleteByStatusAndExpiresAtBefore(
                TokenStatus.EXPIRED,
                LocalDateTime.now()
        );
    }

    private void invalidatePreviousToken(User user) {

        tokenRepository.findByUserAndPurposeAndStatus(
                        user,
                        OtpPurpose.EMAIL_VERIFICATION,
                        TokenStatus.ACTIVE
                )
                .ifPresent(token -> {

                    token.setStatus(TokenStatus.REVOKED);

                    tokenRepository.save(token);
                });
    }

    private EmailVerificationToken createVerificationToken(
            User user,
            String otp,
            EmailVerificationToken previousToken
    ) {

        LocalDateTime now = LocalDateTime.now();

        EmailVerificationToken token = EmailVerificationToken.builder()
                .user(user)
                .otp(otp)
                .purpose(OtpPurpose.EMAIL_VERIFICATION)
                .expiresAt(now.plusMinutes(
                        otpProperties.getExpiryMinutes()))
                .lastSentAt(now)
                .build();

        if (previousToken != null) {
            token.setResendCount(previousToken.getResendCount() + 1);
        }

        return token;
    }

    private boolean isExpired(EmailVerificationToken token) {

        return token.getExpiresAt().isBefore(LocalDateTime.now());
    }

    private void markExpired(EmailVerificationToken token) {

        token.setStatus(TokenStatus.EXPIRED);

        tokenRepository.save(token);
    }

    private void markVerified(EmailVerificationToken token) {

        token.setStatus(TokenStatus.VERIFIED);
        token.setVerifiedAt(LocalDateTime.now());

        tokenRepository.save(token);
    }

    private void incrementFailedAttempts(
            EmailVerificationToken token
    ) {

        token.setFailedAttempts(
                token.getFailedAttempts() + 1
        );

        tokenRepository.save(token);
    }
    private void validateResendRules(
            EmailVerificationToken token
    ) {

        if (token == null) {
            return;
        }

        if (token.getLastSentAt()
                .plusSeconds(
                        otpProperties.getResendCooldownSeconds())
                .isAfter(LocalDateTime.now())) {

            throw new OtpResendCooldownException(
                    "Please wait before requesting another OTP."
            );
        }

        if (token.getResendCount() >=
                otpProperties.getMaxResendAttempts()) {

            throw new TooManyOtpRequestsException(
                    "Maximum resend attempts exceeded."
            );
        }
    }

}