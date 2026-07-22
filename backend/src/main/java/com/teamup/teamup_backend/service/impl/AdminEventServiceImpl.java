package com.teamup.teamup_backend.service.impl;



import com.teamup.teamup_backend.constant.ApiMessages;
import com.teamup.teamup_backend.dto.request.CreateEventRequest;
import com.teamup.teamup_backend.dto.request.UpdateEventRequest;
import com.teamup.teamup_backend.dto.response.AdminEventResponse;
import com.teamup.teamup_backend.entity.Event;
import com.teamup.teamup_backend.enums.Role;
import com.teamup.teamup_backend.exception.BadRequestException;
import com.teamup.teamup_backend.exception.ForbiddenException;
import com.teamup.teamup_backend.exception.ResourceAlreadyExistsException;
import com.teamup.teamup_backend.mapper.AdminEventMapper;
import com.teamup.teamup_backend.repository.EventRepository;
import com.teamup.teamup_backend.service.AdminEventService;
import com.teamup.teamup_backend.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.teamup.teamup_backend.exception.ResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminEventServiceImpl implements AdminEventService {

    private final EventRepository eventRepository;
    private final CurrentUserService currentUserService;

    @Override
    public AdminEventResponse createEvent(CreateEventRequest request) {

        validateAdminAccess();

        validateCreateRequest(request);

        if (eventRepository.existsByTitleIgnoreCase(request.getTitle())) {
            throw new ResourceAlreadyExistsException(
                    ApiMessages.EVENT_ALREADY_EXISTS
            );
        }

        Event event = AdminEventMapper.toEntity(request);

        Event savedEvent = eventRepository.save(event);

        return AdminEventMapper.toResponse(savedEvent);
    }

    @Override
    public AdminEventResponse updateEvent(
            Long eventId,
            UpdateEventRequest request
    ) {

        validateAdminAccess();

        Event event = getEventOrThrow(eventId);

        validateUpdateRequest(request);

        if (eventRepository.existsByTitleIgnoreCaseAndIdNot(
                request.getTitle(),
                eventId
        )) {
            throw new ResourceAlreadyExistsException(
                    ApiMessages.EVENT_ALREADY_EXISTS
            );
        }

        AdminEventMapper.updateEntity(event, request);

        Event updatedEvent = eventRepository.save(event);

        return AdminEventMapper.toResponse(updatedEvent);
    }

    @Override
    public void deleteEvent(Long eventId) {

        validateAdminAccess();

        Event event = getEventOrThrow(eventId);

        if (event.getTeams() != null && !event.getTeams().isEmpty()) {
            throw new BadRequestException(
                    ApiMessages.EVENT_HAS_TEAMS
            );
        }

        eventRepository.delete(event);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminEventResponse getEvent(Long eventId) {

        validateAdminAccess();

        Event event = getEventOrThrow(eventId);

        return AdminEventMapper.toResponse(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminEventResponse> getAllEvents() {

        validateAdminAccess();

        List<Event> events = eventRepository.findAll();

        return AdminEventMapper.toResponseList(events);
    }


    private void validateAdminAccess() {

        if (currentUserService.getCurrentUser().getRole() != Role.ADMIN) {
            throw new ForbiddenException(ApiMessages.ACCESS_DENIED);
        }
    }

    private void validateCreateRequest(CreateEventRequest request) {

        validateTeamSize(
                request.getMinTeamSize(),
                request.getMaxTeamSize()
        );

        validateEventDates(
                request.getRegistrationStart(),
                request.getRegistrationEnd(),
                request.getEventStart(),
                request.getEventEnd()
        );
    }

    private void validateTeamSize(
            Integer minTeamSize,
            Integer maxTeamSize
    ) {

        if (maxTeamSize < minTeamSize) {
            throw new BadRequestException(
                    ApiMessages.INVALID_TEAM_SIZE
            );
        }
    }

    private void validateEventDates(
            java.time.LocalDateTime registrationStart,
            java.time.LocalDateTime registrationEnd,
            java.time.LocalDateTime eventStart,
            java.time.LocalDateTime eventEnd
    ) {

        if (registrationStart.isAfter(registrationEnd)
                || registrationStart.isEqual(registrationEnd)) {

            throw new BadRequestException(
                    ApiMessages.INVALID_REGISTRATION_DATES
            );
        }

        if (eventStart.isAfter(eventEnd)
                || eventStart.isEqual(eventEnd)) {

            throw new BadRequestException(
                    ApiMessages.INVALID_EVENT_DATES
            );
        }

        if (registrationEnd.isAfter(eventStart)) {
            throw new BadRequestException(
                    ApiMessages.REGISTRATION_MUST_END_BEFORE_EVENT
            );
        }
    }
    private Event getEventOrThrow(Long eventId) {

        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ApiMessages.EVENT_NOT_FOUND
                ));
    }
    private void validateUpdateRequest(UpdateEventRequest request) {

        validateTeamSize(
                request.getMinTeamSize(),
                request.getMaxTeamSize()
        );

        validateEventDates(
                request.getRegistrationStart(),
                request.getRegistrationEnd(),
                request.getEventStart(),
                request.getEventEnd()
        );
    }
}
