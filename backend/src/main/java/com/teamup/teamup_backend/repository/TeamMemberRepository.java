package com.teamup.teamup_backend.repository;

import com.teamup.teamup_backend.entity.Team;
import com.teamup.teamup_backend.entity.TeamMember;
import com.teamup.teamup_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    List<TeamMember> findByTeam(Team team);

    List<TeamMember> findByUser(User user);

    Optional<TeamMember> findByTeamAndUser(Team team, User user);

    boolean existsByTeamAndUser(Team team, User user);

    long countByTeam(Team team);

    void deleteByTeamAndUser(Team team, User user);

}