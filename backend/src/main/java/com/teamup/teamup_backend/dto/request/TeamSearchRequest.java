package com.teamup.teamup_backend.dto.request;

import com.teamup.teamup_backend.enums.TeamStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamSearchRequest {

    private String keyword;

    private Long eventId;

    private Long skillId;

    private Boolean recruitmentOpen;

    private TeamStatus status;
}