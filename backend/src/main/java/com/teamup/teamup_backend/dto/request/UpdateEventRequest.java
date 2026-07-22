package com.teamup.teamup_backend.dto.request;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import com.teamup.teamup_backend.constant.ValidationMessages;
import com.teamup.teamup_backend.enums.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEventRequest {

    @NotBlank(message = ValidationMessages.EVENT_TITLE_REQUIRED)
    @Size(max = 255, message = ValidationMessages.EVENT_TITLE_MAX_LENGTH)
    private String title;

    @Size(max = 5000, message = ValidationMessages.EVENT_DESCRIPTION_MAX_LENGTH)
    private String description;

    @NotBlank(message = ValidationMessages.EVENT_ORGANIZER_REQUIRED)
    @Size(max = 255, message = ValidationMessages.EVENT_ORGANIZER_MAX_LENGTH)
    private String organizer;

    @Size(max = 255, message = ValidationMessages.EVENT_LOCATION_MAX_LENGTH)
    private String location;

    private String eventUrl;

    private String registrationUrl;

    private String bannerUrl;

    @NotNull(message = ValidationMessages.EVENT_MODE_REQUIRED)
    private EventMode mode;

    @NotNull(message = ValidationMessages.EVENT_STATUS_REQUIRED)
    private EventStatus status;

    @NotNull(message = ValidationMessages.EVENT_TYPE_REQUIRED)
    private EventType type;

    @NotNull(message = ValidationMessages.EVENT_MIN_TEAM_SIZE_REQUIRED)
    @Min(value = 1, message = ValidationMessages.EVENT_MIN_TEAM_SIZE_INVALID)
    private Integer minTeamSize;

    @NotNull(message = ValidationMessages.EVENT_MAX_TEAM_SIZE_REQUIRED)
    @Min(value = 1, message = ValidationMessages.EVENT_MAX_TEAM_SIZE_INVALID)
    private Integer maxTeamSize;

    @NotNull(message = ValidationMessages.REGISTRATION_START_REQUIRED)
    private LocalDateTime registrationStart;

    @NotNull(message = ValidationMessages.REGISTRATION_END_REQUIRED)
    private LocalDateTime registrationEnd;

    @NotNull(message = ValidationMessages.EVENT_START_REQUIRED)
    private LocalDateTime eventStart;

    @NotNull(message = ValidationMessages.EVENT_END_REQUIRED)
    private LocalDateTime eventEnd;
}