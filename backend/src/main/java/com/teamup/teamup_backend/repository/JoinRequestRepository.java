package com.teamup.teamup_backend.repository;

import com.teamup.teamup_backend.entity.JoinRequest;
import com.teamup.teamup_backend.entity.Team;
import com.teamup.teamup_backend.entity.User;
import com.teamup.teamup_backend.enums.JoinRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JoinRequestRepository extends JpaRepository<JoinRequest, Long> {

    List<JoinRequest> findByTeam(Team team);

    List<JoinRequest> findByUser(User user);

    List<JoinRequest> findByTeamAndStatus(Team team, JoinRequestStatus status);

    Optional<JoinRequest> findByTeamAndUser(Team team, User user);

    boolean existsByTeamAndUser(Team team, User user);

    boolean existsByTeamAndUserAndStatus(
            Team team,
            User user,
            JoinRequestStatus status
    );

    long countByTeamAndStatus(
            Team team,
            JoinRequestStatus status
    );
}