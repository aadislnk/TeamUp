package com.teamup.teamup_backend.mapper;

import com.teamup.teamup_backend.dto.request.CreateTeamRequest;
import com.teamup.teamup_backend.dto.request.UpdateTeamRequest;
import com.teamup.teamup_backend.dto.response.TeamResponse;
import com.teamup.teamup_backend.dto.response.TeamSummaryResponse;
import com.teamup.teamup_backend.entity.Event;
import com.teamup.teamup_backend.entity.Team;
import com.teamup.teamup_backend.entity.User;
import com.teamup.teamup_backend.enums.TeamStatus;
import org.springframework.stereotype.Component;

@Component
public class TeamMapper {

    public TeamResponse toResponse(Team team) {

        if (team == null) {
            return null;
        }

        return TeamResponse.builder()
                .id(team.getId())
                .name(team.getName())
                .description(team.getDescription())
                .currentMembers(team.getCurrentMembers())
                .maxMembers(team.getMaxMembers())
                .recruitmentOpen(team.getRecruitmentOpen())
                .whatsappGroupLink(team.getWhatsappGroupLink())
                .status(team.getStatus())
                .leaderId(team.getLeader().getId())
                .leaderName(team.getLeader().getFullName())
                .eventId(team.getEvent().getId())
                .eventTitle(team.getEvent().getTitle())
                .createdAt(team.getCreatedAt())
                .updatedAt(team.getUpdatedAt())
                .build();
    }

    public TeamSummaryResponse toSummaryResponse(Team team) {

        if (team == null) {
            return null;
        }

        return TeamSummaryResponse.builder()
                .id(team.getId())
                .name(team.getName())
                .currentMembers(team.getCurrentMembers())
                .maxMembers(team.getMaxMembers())
                .recruitmentOpen(team.getRecruitmentOpen())
                .status(team.getStatus())
                .leaderName(team.getLeader().getFullName())
                .eventTitle(team.getEvent().getTitle())
                .build();
    }

    public Team createEntity(
            CreateTeamRequest request,
            Event event,
            User leader
    ) {

        if (request == null) {
            return null;
        }

        return Team.builder()
                .name(request.getName())
                .description(request.getDescription())
                .maxMembers(request.getMaxMembers())
                .currentMembers(1)
                .recruitmentOpen(request.getRecruitmentOpen())
                .whatsappGroupLink(request.getWhatsappGroupLink())
                .status(TeamStatus.OPEN)
                .event(event)
                .leader(leader)
                .build();
    }

    public void updateEntity(
            Team team,
            UpdateTeamRequest request
    ) {

        if (team == null || request == null) {
            return;
        }

        team.setName(request.getName());
        team.setDescription(request.getDescription());
        team.setMaxMembers(request.getMaxMembers());
        team.setRecruitmentOpen(request.getRecruitmentOpen());
        team.setWhatsappGroupLink(request.getWhatsappGroupLink());
    }
}