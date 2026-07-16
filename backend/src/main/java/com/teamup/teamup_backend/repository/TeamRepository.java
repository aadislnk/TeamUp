package com.teamup.teamup_backend.repository;

import com.teamup.teamup_backend.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team,Long> {
}
