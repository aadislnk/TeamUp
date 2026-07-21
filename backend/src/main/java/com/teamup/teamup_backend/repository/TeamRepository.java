package com.teamup.teamup_backend.repository;

import com.teamup.teamup_backend.entity.Event;
import com.teamup.teamup_backend.entity.Team;
import com.teamup.teamup_backend.entity.User;
import com.teamup.teamup_backend.enums.TeamStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long>, JpaSpecificationExecutor<Team> {

    boolean existsByNameIgnoreCaseAndEvent(
            String name,
            Event event
    );

    boolean existsByNameIgnoreCaseAndEventAndIdNot(
            String name,
            Event event,
            Long id
    );

    Page<Team> findByLeader(User leader, Pageable pageable);

    Page<Team> findByStatus(TeamStatus status, Pageable pageable);

    Page<Team> findByRecruitmentOpenTrue(Pageable pageable);

    Optional<Team> findByIdAndLeader(Long id, User leader);

    boolean existsByLeaderAndEvent(User leader, Event event);
}