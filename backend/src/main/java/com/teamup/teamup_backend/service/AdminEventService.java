package com.teamup.teamup_backend.service;


import com.teamup.teamup_backend.dto.request.CreateEventRequest;
import com.teamup.teamup_backend.dto.request.UpdateEventRequest;
import com.teamup.teamup_backend.dto.response.AdminEventResponse;

import java.util.List;

public interface AdminEventService {

    AdminEventResponse createEvent(CreateEventRequest request);

    AdminEventResponse updateEvent(
            Long eventId,
            UpdateEventRequest request
    );

    void deleteEvent(Long eventId);

    AdminEventResponse getEvent(Long eventId);

    List<AdminEventResponse> getAllEvents();
}
