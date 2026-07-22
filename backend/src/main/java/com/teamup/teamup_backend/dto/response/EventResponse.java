package com.teamup.teamup_backend.dto.response;

import com.teamup.teamup_backend.enums.EventMode;
import com.teamup.teamup_backend.enums.EventStatus;
import com.teamup.teamup_backend.enums.EventType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventResponse {

    private Long id;

    private String title;

    private String description;

    private String organizer;

    private String location;

    private String eventUrl;

    private String registrationUrl;

    private String bannerUrl;

    private EventType type;

    private EventMode mode;

    private EventStatus status;

    private LocalDateTime registrationStart;

    private LocalDateTime registrationEnd;

    private LocalDateTime eventStart;

    private LocalDateTime eventEnd;

    private Boolean registrationOpen;

    private Integer minTeamSize;

    private Integer maxTeamSize;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
