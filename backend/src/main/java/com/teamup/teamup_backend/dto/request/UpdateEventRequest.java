package com.teamup.teamup_backend.dto.request;

import com.teamup.teamup_backend.enums.EventMode;
import com.teamup.teamup_backend.enums.EventStatus;
import com.teamup.teamup_backend.enums.EventType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEventRequest {

    @NotBlank(message = "{validation.required}")
    @Size(min = 5, max = 255, message = "{validation.size}")
    private String title;

    @Size(max = 5000, message = "{validation.size}")
    private String description;

    @NotBlank(message = "{validation.required}")
    @Size(max = 255, message = "{validation.size}")
    private String organizer;

    @Size(max = 255, message = "{validation.size}")
    private String location;

    @Size(max = 500, message = "{validation.size}")
    @Pattern(regexp = "^(https?://.*)?$", message = "{validation.invalid.url}")
    private String eventUrl;

    @Size(max = 500, message = "{validation.size}")
    @Pattern(regexp = "^(https?://.*)?$", message = "{validation.invalid.url}")
    private String registrationUrl;

    @Size(max = 500, message = "{validation.size}")
    @Pattern(regexp = "^(https?://.*)?$", message = "{validation.invalid.url}")
    private String bannerUrl;

    @NotNull(message = "{validation.required}")
    private EventType type;

    @NotNull(message = "{validation.required}")
    private EventMode mode;

    @NotNull(message = "{validation.required}")
    private EventStatus status;

    @NotNull(message = "{validation.required}")
    @Future(message = "{validation.future}")
    private LocalDateTime registrationDeadline;

    @NotNull(message = "{validation.required}")
    @Future(message = "{validation.future}")
    private LocalDateTime eventDate;

    @NotNull(message = "{validation.required}")
    private Boolean registrationOpen;

    @NotNull(message = "{validation.required}")
    @Min(value = 1, message = "{validation.min}")
    private Integer minTeamSize;

    @NotNull(message = "{validation.required}")
    @Min(value = 1, message = "{validation.min}")
    private Integer maxTeamSize;
}