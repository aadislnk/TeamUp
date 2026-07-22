package com.teamup.teamup_backend.service.impl;

import com.teamup.teamup_backend.dto.request.CreateEventRequest;
import com.teamup.teamup_backend.dto.request.UpdateEventRequest;
import com.teamup.teamup_backend.dto.response.EventResponse;
import com.teamup.teamup_backend.entity.Event;
import com.teamup.teamup_backend.entity.User;
import com.teamup.teamup_backend.enums.EventMode;
import com.teamup.teamup_backend.enums.EventStatus;
import com.teamup.teamup_backend.enums.EventType;
import com.teamup.teamup_backend.exception.ForbiddenException;
import com.teamup.teamup_backend.mapper.EventMapper;
import com.teamup.teamup_backend.repository.EventRepository;
import com.teamup.teamup_backend.service.CurrentUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @Spy
    private EventMapper eventMapper;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private EventServiceImpl eventService;

    @Test
    void createEventAssignsCurrentUserAsOwner() {

        User owner = user(1L, "Owner User");
        CreateEventRequest request = createRequest("TeamUp Hackathon");

        when(currentUserService.getCurrentUser()).thenReturn(owner);
        when(eventRepository.existsByTitleIgnoreCase(request.getTitle()))
                .thenReturn(false);
        when(eventRepository.save(any(Event.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        EventResponse response = eventService.createEvent(request);

        assertEquals(owner.getId(), response.getOwnerId());
        assertEquals(owner.getFullName(), response.getOwnerName());
    }

    @Test
    void updateEventAllowsOwner() {

        User owner = user(1L, "Owner User");
        Event event = event(10L, owner);
        UpdateEventRequest request = updateRequest("Updated Event");

        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
        when(currentUserService.getCurrentUser()).thenReturn(owner);
        when(eventRepository.existsByTitleIgnoreCaseAndIdNot(
                request.getTitle(),
                event.getId()
        )).thenReturn(false);
        when(eventRepository.save(event)).thenReturn(event);

        EventResponse response = eventService.updateEvent(event.getId(), request);

        assertEquals(request.getTitle(), response.getTitle());
        assertEquals(owner.getId(), response.getOwnerId());
    }

    @Test
    void updateEventRejectsNonOwner() {

        Event event = event(10L, user(1L, "Owner User"));
        User nonOwner = user(2L, "Other User");

        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
        when(currentUserService.getCurrentUser()).thenReturn(nonOwner);

        assertThrows(
                ForbiddenException.class,
                () -> eventService.updateEvent(event.getId(), updateRequest("Blocked Event"))
        );

        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void deleteEventAllowsOwner() {

        User owner = user(1L, "Owner User");
        Event event = event(10L, owner);

        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
        when(currentUserService.getCurrentUser()).thenReturn(owner);

        eventService.deleteEvent(event.getId());

        verify(eventRepository).delete(event);
    }

    @Test
    void deleteEventRejectsNonOwner() {

        Event event = event(10L, user(1L, "Owner User"));
        User nonOwner = user(2L, "Other User");

        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
        when(currentUserService.getCurrentUser()).thenReturn(nonOwner);

        assertThrows(
                ForbiddenException.class,
                () -> eventService.deleteEvent(event.getId())
        );

        verify(eventRepository, never()).delete(any(Event.class));
    }

    private CreateEventRequest createRequest(String title) {

        LocalDateTime now = LocalDateTime.now();

        return CreateEventRequest.builder()
                .title(title)
                .description("A collaborative coding event.")
                .organizer("TeamUp")
                .location("Online")
                .eventUrl("https://teamup.example/events")
                .registrationUrl("https://teamup.example/register")
                .bannerUrl("https://teamup.example/banner.png")
                .mode(EventMode.ONLINE)
                .status(EventStatus.UPCOMING)
                .type(EventType.HACKATHON)
                .minTeamSize(2)
                .maxTeamSize(4)
                .registrationStart(now.minusDays(1))
                .registrationEnd(now.plusDays(1))
                .eventStart(now.plusDays(2))
                .eventEnd(now.plusDays(3))
                .build();
    }

    private UpdateEventRequest updateRequest(String title) {

        LocalDateTime now = LocalDateTime.now();

        return UpdateEventRequest.builder()
                .title(title)
                .description("Updated event details.")
                .organizer("TeamUp")
                .location("Online")
                .eventUrl("https://teamup.example/events")
                .registrationUrl("https://teamup.example/register")
                .bannerUrl("https://teamup.example/banner.png")
                .mode(EventMode.ONLINE)
                .status(EventStatus.UPCOMING)
                .type(EventType.HACKATHON)
                .minTeamSize(2)
                .maxTeamSize(4)
                .registrationStart(now.minusDays(1))
                .registrationEnd(now.plusDays(1))
                .eventStart(now.plusDays(2))
                .eventEnd(now.plusDays(3))
                .build();
    }

    private Event event(Long id, User owner) {

        Event event = eventMapper.createEntity(createRequest("Original Event"), owner);
        event.setId(id);

        return event;
    }

    private User user(Long id, String fullName) {

        User user = User.builder()
                .fullName(fullName)
                .email("user" + id + "@teamup.example")
                .password("password")
                .college("TeamUp College")
                .build();
        user.setId(id);

        return user;
    }
}
