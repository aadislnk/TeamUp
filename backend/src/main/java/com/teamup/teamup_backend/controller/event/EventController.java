package com.teamup.teamup_backend.controller.event;

import com.teamup.teamup_backend.constant.ApiPaths;
import com.teamup.teamup_backend.dto.request.CreateEventRequest;
import com.teamup.teamup_backend.dto.request.UpdateEventRequest;
import com.teamup.teamup_backend.dto.response.EventResponse;
import com.teamup.teamup_backend.dto.response.EventSummaryResponse;
import com.teamup.teamup_backend.enums.EventMode;
import com.teamup.teamup_backend.enums.EventStatus;
import com.teamup.teamup_backend.enums.EventType;
import com.teamup.teamup_backend.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiPaths.EVENTS)
public class EventController {

    private final EventService eventService;

    //public api endpts
    @GetMapping
    public Page<EventResponse> getAllEvents(Pageable pageable) {

        return eventService.getAllEvents(pageable);
    }

    @GetMapping("/{eventId}")
    public EventResponse getEventById(
            @PathVariable Long eventId
    ) {

        return eventService.getEventById(eventId);
    }

    @GetMapping("/search")
    public Page<EventSummaryResponse> searchEvents(

            @RequestParam(required = false) String keyword,

            @RequestParam(required = false) EventType type,

            @RequestParam(required = false) EventMode mode,

            @RequestParam(required = false) EventStatus status,

            @RequestParam(required = false) Boolean registrationOpen,

            Pageable pageable
    ) {

        if (keyword != null && !keyword.isBlank()) {
            return eventService.searchEvents(keyword, pageable);
        }

        return eventService.filterEvents(
                type,
                mode,
                status,
                registrationOpen,
                pageable
        );
    }

    @GetMapping("/upcoming")
    public Page<EventSummaryResponse> getUpcomingEvents(
            Pageable pageable
    ) {

        return eventService.getUpcomingEvents(pageable);
    }

    //admin apis:

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponse createEvent(
            @Valid @RequestBody CreateEventRequest request
    ) {

        return eventService.createEvent(request);
    }

    @PutMapping("/{eventId}")
    public EventResponse updateEvent(

            @PathVariable Long eventId,

            @Valid @RequestBody UpdateEventRequest request
    ) {

        return eventService.updateEvent(eventId, request);
    }

    @DeleteMapping("/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(
            @PathVariable Long eventId
    ) {

        eventService.deleteEvent(eventId);
    }

}