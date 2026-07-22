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
public class EventSummaryResponse {

    private Long id;

    private String title;

    private String organizer;

    private String location;

    private String bannerUrl;

    private Long ownerId;

    private String ownerName;

    private String ownerProfileImage;

    private EventType type;

    private EventMode mode;

    private EventStatus status;

    private LocalDateTime eventStart;

    private LocalDateTime eventEnd;

    private Boolean registrationOpen;
}
