package com.teamup.teamup_backend.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaderDashboardResponse {

    private TeamResponse team;

    private Integer pendingRequestsCount;

    private Integer acceptedMembers;

    private Boolean recruitmentOpen;
}
