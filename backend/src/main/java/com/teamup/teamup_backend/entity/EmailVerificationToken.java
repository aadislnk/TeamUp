package com.teamup.teamup_backend.entity;

import com.teamup.teamup_backend.enums.OtpPurpose;
import com.teamup.teamup_backend.enums.TokenStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "email_verification_tokens",
        indexes = {
                @Index(name = "idx_email_verification_user", columnList = "user_id")
                ,
                @Index(
                        name = "idx_email_verification_status",
                        columnList = "status"
                ),
                @Index(
                        name = "idx_email_verification_expires_at",
                        columnList = "expires_at"
                )
        }
)
public class EmailVerificationToken extends BaseEntity {

    @Column(nullable = false,length = 6)
    private String otp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OtpPurpose purpose;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TokenStatus status = TokenStatus.ACTIVE;

    @Column
    private LocalDateTime verifiedAt;

    @Column(name = "expires_at",nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private Integer resendCount = 0;

    @Column(nullable = false)
    private Integer failedAttempts = 0;

    @Column(nullable = false)
    private LocalDateTime lastSentAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
