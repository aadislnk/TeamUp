package com.teamup.teamup_backend.repository;

import com.teamup.teamup_backend.entity.Skill;
import com.teamup.teamup_backend.entity.Team;
import com.teamup.teamup_backend.entity.TeamSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamSkillRepository extends JpaRepository<TeamSkill, Long> {

    boolean existsByTeamAndSkill(
            Team team,
            Skill skill
    );

    List<TeamSkill> findByTeam(
            Team team
    );

    Optional<TeamSkill> findByTeamAndSkill(
            Team team,
            Skill skill
    );

    void deleteByTeamAndSkill(
            Team team,
            Skill skill
    );

    long countByTeam(
            Team team
    );

}
