package com.teamup.teamup_backend.dto.response;

import com.teamup.teamup_backend.enums.TeamStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamSummaryResponse {

    private Long id;

    private String name;

    private Integer currentMembers;

    private Integer maxMembers;

    private Boolean recruitmentOpen;

    private TeamStatus status;

    private String leaderName;

    private String eventTitle;
}