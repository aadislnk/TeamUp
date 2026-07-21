package com.teamup.teamup_backend.service;

import com.teamup.teamup_backend.dto.request.CreateEventRequest;
import com.teamup.teamup_backend.dto.request.UpdateEventRequest;
import com.teamup.teamup_backend.dto.response.EventResponse;
import com.teamup.teamup_backend.dto.response.EventSummaryResponse;
import com.teamup.teamup_backend.enums.EventMode;
import com.teamup.teamup_backend.enums.EventStatus;
import com.teamup.teamup_backend.enums.EventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventService {

    Page<EventResponse> getAllEvents(Pageable pageable);

    EventResponse getEventById(Long eventId);

    EventResponse createEvent(CreateEventRequest request);

    EventResponse updateEvent(Long eventId, UpdateEventRequest request);

    void deleteEvent(Long eventId);

    Page<EventSummaryResponse> searchEvents(
            String keyword,
            Pageable pageable
    );

    Page<EventSummaryResponse> filterEvents(
            EventType type,
            EventMode mode,
            EventStatus status,
            Boolean registrationOpen,
            Pageable pageable
    );

    Page<EventSummaryResponse> getUpcomingEvents(Pageable pageable);
}