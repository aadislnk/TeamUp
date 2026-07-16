package com.teamup.teamup_backend.repository;

import com.teamup.teamup_backend.entity.JoinRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JoinRequestRepository extends JpaRepository<JoinRequest,Long> {
}
