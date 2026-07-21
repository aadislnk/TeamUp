package com.teamup.teamup_backend.repository;

import com.teamup.teamup_backend.entity.Event;
import com.teamup.teamup_backend.enums.EventMode;
import com.teamup.teamup_backend.enums.EventStatus;
import com.teamup.teamup_backend.enums.EventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;

public interface EventRepository extends
        JpaRepository<Event, Long>,
        JpaSpecificationExecutor<Event> {

    Page<Event> findByTitleContainingIgnoreCase(
            String title,
            Pageable pageable
    );
    Page<Event> findByType(
            EventType type,
            Pageable pageable
    );

    Page<Event> findByMode(
            EventMode mode,
            Pageable pageable
    );

    Page<Event> findByStatus(
            EventStatus status,
            Pageable pageable
    );

    Page<Event> findByEventDateAfter(
            LocalDateTime dateTime,
            Pageable pageable
    );

    Page<Event> findByRegistrationOpenTrue(
            Pageable pageable
    );
}