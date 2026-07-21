package com.teamup.teamup_backend.dto.response;

import com.teamup.teamup_backend.enums.TeamStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamResponse {

    private Long id;

    private String name;

    private String description;

    private Integer currentMembers;

    private Integer maxMembers;

    private Boolean recruitmentOpen;

    private String whatsappGroupLink;

    private TeamStatus status;

    private Long leaderId;

    private String leaderName;

    private Long eventId;

    private String eventTitle;

    private List<SkillResponse> requiredSkills;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}