package com.teamup.teamup_backend.mapper;


import com.teamup.teamup_backend.dto.request.CreateEventRequest;
import com.teamup.teamup_backend.dto.request.UpdateEventRequest;
import com.teamup.teamup_backend.dto.response.AdminEventResponse;
import com.teamup.teamup_backend.entity.Event;

import java.util.List;

public final class AdminEventMapper {

    private AdminEventMapper() {
    }

    public static Event toEntity(CreateEventRequest request) {
        return Event.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .organizer(request.getOrganizer())
                .location(request.getLocation())
                .eventUrl(request.getEventUrl())
                .registrationUrl(request.getRegistrationUrl())
                .bannerUrl(request.getBannerUrl())
                .mode(request.getMode())
                .status(request.getStatus())
                .type(request.getType())
                .minTeamSize(request.getMinTeamSize())
                .maxTeamSize(request.getMaxTeamSize())
                .registrationOpen(isRegistrationCurrentlyOpen(request.getRegistrationStart(), request.getRegistrationEnd()))
                .registrationStart(request.getRegistrationStart())
                .registrationEnd(request.getRegistrationEnd())
                .eventStart(request.getEventStart())
                .eventEnd(request.getEventEnd())
                .build();
    }

    public static void updateEntity(Event event, UpdateEventRequest request) {
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setOrganizer(request.getOrganizer());
        event.setLocation(request.getLocation());
        event.setEventUrl(request.getEventUrl());
        event.setRegistrationUrl(request.getRegistrationUrl());
        event.setBannerUrl(request.getBannerUrl());
        event.setMode(request.getMode());
        event.setStatus(request.getStatus());
        event.setType(request.getType());
        event.setMinTeamSize(request.getMinTeamSize());
        event.setMaxTeamSize(request.getMaxTeamSize());
        event.setRegistrationOpen(isRegistrationCurrentlyOpen(request.getRegistrationStart(), request.getRegistrationEnd()));
        event.setRegistrationStart(request.getRegistrationStart());
        event.setRegistrationEnd(request.getRegistrationEnd());
        event.setEventStart(request.getEventStart());
        event.setEventEnd(request.getEventEnd());
    }

    public static AdminEventResponse toResponse(Event event) {
        return AdminEventResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .organizer(event.getOrganizer())
                .location(event.getLocation())
                .eventUrl(event.getEventUrl())
                .registrationUrl(event.getRegistrationUrl())
                .bannerUrl(event.getBannerUrl())
                .mode(event.getMode())
                .status(event.getStatus())
                .type(event.getType())
                .minTeamSize(event.getMinTeamSize())
                .maxTeamSize(event.getMaxTeamSize())
                .registrationStart(event.getRegistrationStart())
                .registrationEnd(event.getRegistrationEnd())
                .eventStart(event.getEventStart())
                .eventEnd(event.getEventEnd())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .build();
    }

    public static List<AdminEventResponse> toResponseList(List<Event> events) {
        return events.stream()
                .map(AdminEventMapper::toResponse)
                .toList();
    }

    private static boolean isRegistrationCurrentlyOpen(
            java.time.LocalDateTime registrationStart,
            java.time.LocalDateTime registrationEnd
    ) {

        java.time.LocalDateTime now = java.time.LocalDateTime.now();

        return !now.isBefore(registrationStart)
                && !now.isAfter(registrationEnd);
    }
}
