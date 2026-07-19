package com.teamup.teamup_backend.repository;

import com.teamup.teamup_backend.entity.EmailVerificationToken;
import com.teamup.teamup_backend.entity.User;
import com.teamup.teamup_backend.enums.OtpPurpose;
import com.teamup.teamup_backend.enums.TokenStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EmailVerificationTokenRepository
        extends JpaRepository<EmailVerificationToken, Long> {
    Optional<EmailVerificationToken> findByUserAndPurposeAndStatus( User user,
                                                                    OtpPurpose purpose,
                                                                    TokenStatus status);

    Optional<EmailVerificationToken> findTopByUserAndPurposeOrderByCreatedAtDesc(User user,
                                                                                 OtpPurpose purpose);

    List<EmailVerificationToken> findByStatusAndExpiresAtBefore(TokenStatus status, LocalDateTime expiresAt);

    boolean existsByUserAndPurposeAndStatus(User user,OtpPurpose purpose,TokenStatus status);

    long countByUserAndPurposeAndCreatedAtAfter( User user,
                                                 OtpPurpose purpose,
                                                 LocalDateTime createdAt);

    void deleteByStatusAndExpiresAtBefore(TokenStatus status, LocalDateTime expiresAt);
}