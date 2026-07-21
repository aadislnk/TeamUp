package com.teamup.teamup_backend.dto.request;

import com.teamup.teamup_backend.constant.ApplicationConstants;
import com.teamup.teamup_backend.constant.ValidationMessages;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTeamRequest {

    @NotBlank(message = ValidationMessages.TEAM_NAME_REQUIRED)
    @Size(
            min = ApplicationConstants.TEAM_NAME_MIN_LENGTH,
            max = ApplicationConstants.TEAM_NAME_MAX_LENGTH,
            message = ValidationMessages.TEAM_NAME_MAX
    )
    private String name;

    @Size(
            max = ApplicationConstants.TEAM_DESCRIPTION_MAX_LENGTH,
            message = ValidationMessages.TEAM_DESCRIPTION_MAX
    )
    private String description;

    @NotNull(message = ValidationMessages.MAX_MEMBERS_REQUIRED)
    @Min(
            value = ApplicationConstants.TEAM_MIN_MEMBERS,
            message = ValidationMessages.MAX_MEMBERS_MIN
    )
    @Max(
            value = ApplicationConstants.TEAM_MAX_MEMBERS,
            message = ValidationMessages.MAX_MEMBERS_MAX
    )
    private Integer maxMembers;

    @NotNull(message = ValidationMessages.RECRUITMENT_STATUS_REQUIRED)
    private Boolean recruitmentOpen;

    @Pattern(
            regexp = "^https://chat\\.whatsapp\\.com/[A-Za-z0-9]+$",
            message = ValidationMessages.WHATSAPP_LINK_INVALID
    )
    private String whatsappGroupLink;

    @NotNull(message = "Event is required.")
    private Long eventId;
}