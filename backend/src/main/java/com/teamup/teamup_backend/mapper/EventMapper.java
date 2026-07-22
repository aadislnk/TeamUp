package com.teamup.teamup_backend.mapper;

import com.teamup.teamup_backend.dto.request.CreateEventRequest;
import com.teamup.teamup_backend.dto.request.UpdateEventRequest;
import com.teamup.teamup_backend.dto.response.EventResponse;
import com.teamup.teamup_backend.dto.response.EventSummaryResponse;
import com.teamup.teamup_backend.entity.Event;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public EventResponse toResponse(Event event) {

        if (event == null) {
            return null;
        }

        return EventResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .organizer(event.getOrganizer())
                .location(event.getLocation())
                .eventUrl(event.getEventUrl())
                .registrationUrl(event.getRegistrationUrl())
                .bannerUrl(event.getBannerUrl())
                .type(event.getType())
                .mode(event.getMode())
                .status(event.getStatus())
                .registrationStart(event.getRegistrationStart())
                .registrationEnd(event.getRegistrationEnd())
                .eventStart(event.getEventStart())
                .eventEnd(event.getEventEnd())
                .registrationOpen(event.getRegistrationOpen())
                .minTeamSize(event.getMinTeamSize())
                .maxTeamSize(event.getMaxTeamSize())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .build();
    }

    public EventSummaryResponse toSummaryResponse(Event event) {

        if (event == null) {
            return null;
        }

        return EventSummaryResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .organizer(event.getOrganizer())
                .location(event.getLocation())
                .bannerUrl(event.getBannerUrl())
                .type(event.getType())
                .mode(event.getMode())
                .status(event.getStatus())
                .eventStart(event.getEventStart())
                .eventEnd(event.getEventEnd())
                .registrationOpen(event.getRegistrationOpen())
                .build();
    }

    public Event createEntity(CreateEventRequest request) {

        if (request == null) {
            return null;
        }

        return Event.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .organizer(request.getOrganizer())
                .location(request.getLocation())
                .eventUrl(request.getEventUrl())
                .registrationUrl(request.getRegistrationUrl())
                .bannerUrl(request.getBannerUrl())
                .type(request.getType())
                .mode(request.getMode())
                .status(request.getStatus())
                .registrationStart(request.getRegistrationStart())
                .registrationEnd(request.getRegistrationEnd())
                .eventStart(request.getEventStart())
                .eventEnd(request.getEventEnd())
                .registrationOpen(isRegistrationCurrentlyOpen(request.getRegistrationStart(), request.getRegistrationEnd()))
                .minTeamSize(request.getMinTeamSize())
                .maxTeamSize(request.getMaxTeamSize())
                .build();
    }

    public void updateEntity(Event event, UpdateEventRequest request) {

        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setOrganizer(request.getOrganizer());
        event.setLocation(request.getLocation());
        event.setEventUrl(request.getEventUrl());
        event.setRegistrationUrl(request.getRegistrationUrl());
        event.setBannerUrl(request.getBannerUrl());
        event.setType(request.getType());
        event.setMode(request.getMode());
        event.setStatus(request.getStatus());
        event.setRegistrationStart(request.getRegistrationStart());
        event.setRegistrationEnd(request.getRegistrationEnd());
        event.setEventStart(request.getEventStart());
        event.setEventEnd(request.getEventEnd());
        event.setRegistrationOpen(isRegistrationCurrentlyOpen(request.getRegistrationStart(), request.getRegistrationEnd()));
        event.setMinTeamSize(request.getMinTeamSize());
        event.setMaxTeamSize(request.getMaxTeamSize());
    }

    private boolean isRegistrationCurrentlyOpen(
            java.time.LocalDateTime registrationStart,
            java.time.LocalDateTime registrationEnd
    ) {

        java.time.LocalDateTime now = java.time.LocalDateTime.now();

        return !now.isBefore(registrationStart)
                && !now.isAfter(registrationEnd);
    }
}
