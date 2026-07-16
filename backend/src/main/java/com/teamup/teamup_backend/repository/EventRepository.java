package com.teamup.teamup_backend.repository;

import com.teamup.teamup_backend.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event,Long> {
}
