package com.teamup.teamup_backend.service.impl;

import com.teamup.teamup_backend.dto.request.CreateEventRequest;
import com.teamup.teamup_backend.dto.request.UpdateEventRequest;
import com.teamup.teamup_backend.dto.response.EventResponse;
import com.teamup.teamup_backend.dto.response.EventSummaryResponse;
import com.teamup.teamup_backend.entity.Event;
import com.teamup.teamup_backend.enums.EventMode;
import com.teamup.teamup_backend.enums.EventStatus;
import com.teamup.teamup_backend.enums.EventType;
import com.teamup.teamup_backend.exception.BadRequestException;
import com.teamup.teamup_backend.exception.ResourceNotFoundException;
import com.teamup.teamup_backend.mapper.EventMapper;
import com.teamup.teamup_backend.repository.EventRepository;
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

        Event event = eventMapper.createEntity(request);

        event = eventRepository.save(event);

        return eventMapper.toResponse(event);
    }

    @Override
    public EventResponse updateEvent(Long eventId,
                                     UpdateEventRequest request) {

        Event event = getEventOrThrow(eventId);

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

        eventMapper.updateEntity(event, request);

        event = eventRepository.save(event);

        return eventMapper.toResponse(event);
    }

    @Override
    public void deleteEvent(Long eventId) {

        Event event = getEventOrThrow(eventId);

        eventRepository.delete(event);
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
                                "Event not found with id: " + eventId
                        ));
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
