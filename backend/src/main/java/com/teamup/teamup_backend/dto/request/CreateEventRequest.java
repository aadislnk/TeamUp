package com.teamup.teamup_backend.dto.request;

import com.teamup.teamup_backend.constant.ValidationMessages;
import com.teamup.teamup_backend.enums.EventMode;
import com.teamup.teamup_backend.enums.EventStatus;
import com.teamup.teamup_backend.enums.EventType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateEventRequest {

    @Schema(
            description = "Event title",
            example = "Smart India Hackathon 2026",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = ValidationMessages.EVENT_TITLE_REQUIRED)
    @Size(max = 255, message = ValidationMessages.EVENT_TITLE_MAX_LENGTH)
    private String title;

    @Schema(
            description = "Event description",
            example = "National level hackathon conducted by Government of India."
    )
    @Size(max = 5000, message = ValidationMessages.EVENT_DESCRIPTION_MAX_LENGTH)
    private String description;

    @Schema(
            description = "Organizer",
            example = "Ministry of Education",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = ValidationMessages.EVENT_ORGANIZER_REQUIRED)
    @Size(max = 255, message = ValidationMessages.EVENT_ORGANIZER_MAX_LENGTH)
    private String organizer;

    @Schema(example = "New Delhi")
    @Size(max = 255, message = ValidationMessages.EVENT_LOCATION_MAX_LENGTH)
    private String location;

    @Schema(example = "https://sih.gov.in")
    private String eventUrl;

    @Schema(example = "https://sih.gov.in/register")
    private String registrationUrl;

    @Schema(example = "https://cdn.teamup.com/banner.png")
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