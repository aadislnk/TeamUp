package com.teamup.teamup_backend.repository;

import com.teamup.teamup_backend.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill,Long> {
}
