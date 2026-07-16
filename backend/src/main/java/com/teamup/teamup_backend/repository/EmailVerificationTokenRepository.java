package com.teamup.teamup_backend.repository;

import com.teamup.teamup_backend.entity.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken,Long> {
}
