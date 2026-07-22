package com.teamup.teamup_backend.controller.admin;

import com.teamup.teamup_backend.constant.ApiMessages;
import com.teamup.teamup_backend.constant.ApiPaths;
import com.teamup.teamup_backend.dto.request.CreateEventRequest;
import com.teamup.teamup_backend.dto.request.UpdateEventRequest;
import com.teamup.teamup_backend.dto.response.AdminEventResponse;
import com.teamup.teamup_backend.dto.common.ApiResponse;
import com.teamup.teamup_backend.service.AdminEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.ADMIN)
@RequiredArgsConstructor
@Tag(name = "Admin Event Management")
@SecurityRequirement(name = "Bearer Authentication")
public class AdminEventController {

    private final AdminEventService adminEventService;

    @PostMapping
    @Operation(summary = "Create a new event")
    public ResponseEntity<ApiResponse<AdminEventResponse>> createEvent(
            @Valid @RequestBody CreateEventRequest request
    ) {

        AdminEventResponse response = adminEventService.createEvent(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        ApiMessages.EVENT_CREATED_SUCCESS,
                        response
                ));
    }

    @PutMapping("/{eventId}")
    @Operation(summary = "Update an existing event")
    public ResponseEntity<ApiResponse<AdminEventResponse>> updateEvent(
            @PathVariable Long eventId,
            @Valid @RequestBody UpdateEventRequest request
    ) {

        AdminEventResponse response =
                adminEventService.updateEvent(eventId, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        ApiMessages.EVENT_UPDATED_SUCCESS,
                        response
                )
        );
    }

    @DeleteMapping("/{eventId}")
    @Operation(summary = "Delete an event")
    public ResponseEntity<ApiResponse<Void>> deleteEvent(
            @PathVariable Long eventId
    ) {

        adminEventService.deleteEvent(eventId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        ApiMessages.EVENT_DELETED_SUCCESS,
                        null
                )
        );
    }

    @GetMapping
    @Operation(summary = "Get all events")
    public ResponseEntity<ApiResponse<List<AdminEventResponse>>> getAllEvents() {

        List<AdminEventResponse> response =
                adminEventService.getAllEvents();

        return ResponseEntity.ok(
                ApiResponse.success(
                        ApiMessages.EVENTS_FETCHED_SUCCESS,
                        response
                )
        );
    }

    @GetMapping("/{eventId}")
    @Operation(summary = "Get event by ID")
    public ResponseEntity<ApiResponse<AdminEventResponse>> getEvent(
            @PathVariable Long eventId
    ) {

        AdminEventResponse response =
                adminEventService.getEvent(eventId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        ApiMessages.EVENT_FETCHED_SUCCESS,
                        response
                )
        );
    }
}