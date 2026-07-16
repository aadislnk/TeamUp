package com.teamup.teamup_backend.entity;

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
        }
)
public class EmailVerificationToken extends BaseEntity {

    @Column(nullable = false,length = 10)
    private String otp;

    @Column(nullable = false)
    private Boolean verified = false;

    @Column(name = "expiry_time",nullable = false)
    private LocalDateTime expiryTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
