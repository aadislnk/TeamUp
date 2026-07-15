package com.teamup.teamup_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "email_verification_tokens")
public class EmailVerificationToken extends BaseEntity {

    @Column(nullable = false,length = 10)
    private String otp;

    @Column(nullable = false)
    private Boolean verified = false;

    @Column(name = "expiry_time",nullable = false)
    private LocalDateTime expiryTime;
}
