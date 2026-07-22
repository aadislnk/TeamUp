package com.teamup.teamup_backend.service.impl;

import com.teamup.teamup_backend.constant.ApiMessages;
import com.teamup.teamup_backend.dto.request.CreateEventRequest;
import com.teamup.teamup_backend.dto.request.UpdateEventRequest;
import com.teamup.teamup_backend.dto.response.EventResponse;
import com.teamup.teamup_backend.dto.response.EventSummaryResponse;
import com.teamup.teamup_backend.entity.Event;
import com.teamup.teamup_backend.entity.User;
import com.teamup.teamup_backend.enums.EventMode;
import com.teamup.teamup_backend.enums.EventStatus;
import com.teamup.teamup_backend.enums.EventType;
import com.teamup.teamup_backend.exception.BadRequestException;
import com.teamup.teamup_backend.exception.ForbiddenException;
import com.teamup.teamup_backend.exception.ResourceAlreadyExistsException;
import com.teamup.teamup_backend.exception.ResourceNotFoundException;
import com.teamup.teamup_backend.mapper.EventMapper;
import com.teamup.teamup_backend.repository.EventRepository;
import com.teamup.teamup_backend.service.CurrentUserService;
import com.teamup.teamup_backend.service.EventService;
import com.teamup.teamup_backend.specification.EventSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional(readOnly = true)
    public Page<EventResponse> getAllEvents(Pageable pageable) {

        return eventRepository
                .findAll(getSortedPageable(pageable))
                .map(eventMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public EventResponse getEventById(Long eventId) {

        Event event = getEventOrThrow(eventId);

        return eventMapper.toResponse(event);
    }

    @Override
    public EventResponse createEvent(CreateEventRequest request) {

        validateDates(
                request.getRegistrationStart(),
                request.getRegistrationEnd(),
                request.getEventStart(),
                request.getEventEnd()
        );

        validateTeamCapacity(
                request.getMinTeamSize(),
                request.getMaxTeamSize()
        );

        validateStatus(
                isRegistrationCurrentlyOpen(
                        request.getRegistrationStart(),
                        request.getRegistrationEnd()
                ),
                request.getStatus()
        );

        if (eventRepository.existsByTitleIgnoreCase(request.getTitle())) {
            throw new ResourceAlreadyExistsException(
                    ApiMessages.EVENT_ALREADY_EXISTS
            );
        }

        User currentUser = currentUserService.getCurrentUser();

        Event event = eventMapper.createEntity(request, currentUser);

        event = eventRepository.save(event);

        return eventMapper.toResponse(event);
    }

    @Override
    public EventResponse updateEvent(Long eventId,
                                     UpdateEventRequest request) {

        Event event = getEventOrThrow(eventId);

        validateEventOwnership(event);

        validateDates(
                request.getRegistrationStart(),
                request.getRegistrationEnd(),
                request.getEventStart(),
                request.getEventEnd()
        );

        validateTeamCapacity(
                request.getMinTeamSize(),
                request.getMaxTeamSize()
        );

        validateStatus(
                isRegistrationCurrentlyOpen(
                        request.getRegistrationStart(),
                        request.getRegistrationEnd()
                ),
                request.getStatus()
        );

        if (eventRepository.existsByTitleIgnoreCaseAndIdNot(
                request.getTitle(),
                eventId
        )) {
            throw new ResourceAlreadyExistsException(
                    ApiMessages.EVENT_ALREADY_EXISTS
            );
        }

        eventMapper.updateEntity(event, request);

        event = eventRepository.save(event);

        return eventMapper.toResponse(event);
    }

    @Override
    public void deleteEvent(Long eventId) {

        Event event = getEventOrThrow(eventId);

        validateEventOwnership(event);

        if (event.getTeams() != null && !event.getTeams().isEmpty()) {
            throw new BadRequestException(
                    ApiMessages.EVENT_HAS_TEAMS
            );
        }

        eventRepository.delete(event);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventResponse> getMyEvents(Pageable pageable) {

        User currentUser = currentUserService.getCurrentUser();

        return eventRepository
                .findByOwner(
                        currentUser,
                        getSortedPageable(pageable)
                )
                .map(eventMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventSummaryResponse> searchEvents(
            String keyword,
            Pageable pageable
    ) {

        Specification<Event> specification = EventSpecifications.search(keyword);

        return eventRepository
                .findAll(specification, getSortedPageable(pageable))
                .map(eventMapper::toSummaryResponse);
    }
    @Override
    @Transactional(readOnly = true)
    public Page<EventSummaryResponse> filterEvents(
            EventType type,
            EventMode mode,
            EventStatus status,
            Boolean registrationOpen,
            Pageable pageable
    ) {

        Specification<Event> specification = Specification
                .where(EventSpecifications.hasType(type))
                .and(EventSpecifications.hasMode(mode))
                .and(EventSpecifications.hasStatus(status))
                .and(EventSpecifications.hasRegistrationOpen(registrationOpen));

        return eventRepository
                .findAll(specification, getSortedPageable(pageable))
                .map(eventMapper::toSummaryResponse);
    }
    @Override
    @Transactional(readOnly = true)
    public Page<EventSummaryResponse> getUpcomingEvents(Pageable pageable) {

        Specification<Event> specification = EventSpecifications.upcoming();

        return eventRepository
                .findAll(specification, getSortedPageable(pageable))
                .map(eventMapper::toSummaryResponse);
    }


    //helper method
    private Event getEventOrThrow(Long eventId) {

        return eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                ApiMessages.EVENT_NOT_FOUND
                        ));
    }

    private void validateEventOwnership(Event event) {

        User currentUser = currentUserService.getCurrentUser();

        if (event.getOwner() == null
                || !event.getOwner().getId().equals(currentUser.getId())) {
            throw new ForbiddenException(ApiMessages.ACCESS_DENIED);
        }
    }

    private void validateDates(
            java.time.LocalDateTime registrationStart,
            java.time.LocalDateTime registrationEnd,
            java.time.LocalDateTime eventStart,
            java.time.LocalDateTime eventEnd
    ) {

        if (!registrationStart.isBefore(registrationEnd)) {

            throw new BadRequestException(
                    "Registration start must be before registration end."
            );
        }

        if (!eventStart.isBefore(eventEnd)) {

            throw new BadRequestException(
                    "Event start must be before event end."
            );
        }

        if (registrationEnd.isAfter(eventStart)) {

            throw new BadRequestException(
                    "Registration end cannot be after event start."
            );
        }
    }

    private void validateTeamCapacity(
            Integer minTeamSize,
            Integer maxTeamSize
    ) {

        if (minTeamSize > maxTeamSize) {

            throw new BadRequestException(
                    "Minimum team size cannot be greater than maximum team size."
            );
        }
    }

    private void validateStatus(
            Boolean registrationOpen,
            com.teamup.teamup_backend.enums.EventStatus status
    ) {

        if (Boolean.TRUE.equals(registrationOpen)
                && status == com.teamup.teamup_backend.enums.EventStatus.COMPLETED) {

            throw new BadRequestException(
                    "Completed events cannot have registration open."
            );
        }
    }

    private Pageable getSortedPageable(Pageable pageable) {

        return PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("eventStart").ascending()
        );
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
