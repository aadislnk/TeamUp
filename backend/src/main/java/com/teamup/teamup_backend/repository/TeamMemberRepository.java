package com.teamup.teamup_backend.repository;

import com.teamup.teamup_backend.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberRepository extends JpaRepository<TeamMember,Long> {
}
