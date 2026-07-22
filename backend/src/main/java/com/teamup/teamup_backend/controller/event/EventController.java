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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Tag(name = "Event Management")
public class EventController {

    private final EventService eventService;

    //public api endpts
    @GetMapping(ApiPaths.EVENTS)
    @Operation(summary = "Get all public events")
    public Page<EventResponse> getAllEvents(Pageable pageable) {

        return eventService.getAllEvents(pageable);
    }

    @GetMapping(ApiPaths.EVENTS + "/{eventId}")
    @Operation(summary = "Get event details")
    public EventResponse getEventById(
            @PathVariable Long eventId
    ) {

        return eventService.getEventById(eventId);
    }

    @GetMapping(ApiPaths.EVENTS + "/search")
    @Operation(summary = "Search and filter events")
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

    @GetMapping(ApiPaths.EVENTS + "/upcoming")
    @Operation(summary = "Get upcoming events")
    public Page<EventSummaryResponse> getUpcomingEvents(
            Pageable pageable
    ) {

        return eventService.getUpcomingEvents(pageable);
    }

    @PostMapping(ApiPaths.EVENTS)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create event",
            description = "Creates an event owned by the authenticated user."
    )
    @SecurityRequirement(name = "bearerAuth")
    public EventResponse createEvent(
            @Valid @RequestBody CreateEventRequest request
    ) {

        return eventService.createEvent(request);
    }

    @PutMapping(ApiPaths.EVENTS + "/{eventId}")
    @Operation(
            summary = "Update own event",
            description = "Only the authenticated event owner can update this event."
    )
    @SecurityRequirement(name = "bearerAuth")
    public EventResponse updateEvent(
            @PathVariable Long eventId,
            @Valid @RequestBody UpdateEventRequest request
    ) {

        return eventService.updateEvent(eventId, request);
    }

    @DeleteMapping(ApiPaths.EVENTS + "/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete own event",
            description = "Only the authenticated event owner can delete this event."
    )
    @SecurityRequirement(name = "bearerAuth")
    public void deleteEvent(
            @PathVariable Long eventId
    ) {

        eventService.deleteEvent(eventId);
    }

    @GetMapping(ApiPaths.USERS + ApiPaths.CURRENT_USER + "/events")
    @Operation(summary = "Get events owned by current user")
    @SecurityRequirement(name = "bearerAuth")
    public Page<EventResponse> getMyEvents(Pageable pageable) {

        return eventService.getMyEvents(pageable);
    }
}
