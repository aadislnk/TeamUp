package com.teamup.teamup_backend.service;

import com.teamup.teamup_backend.dto.request.CreateTeamRequest;
import com.teamup.teamup_backend.dto.request.UpdateTeamRequest;
import com.teamup.teamup_backend.dto.response.SkillResponse;
import com.teamup.teamup_backend.dto.response.TeamResponse;
import com.teamup.teamup_backend.enums.TeamStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TeamService {

    Page<TeamResponse> getAllTeams(Pageable pageable);

    TeamResponse getTeamById(Long teamId);

    TeamResponse createTeam(CreateTeamRequest request);

    TeamResponse updateTeam(Long teamId, UpdateTeamRequest request);

    void deleteTeam(Long teamId);

    Page<TeamResponse> getOpenTeams(Pageable pageable);

    Page<TeamResponse> getTeamsByStatus(
            TeamStatus status,
            Pageable pageable
    );

    Page<TeamResponse> getMyTeams(Pageable pageable);

    TeamResponse updateRecruitmentStatus(
            Long teamId,
            Boolean recruitmentOpen
    );

    TeamResponse addRequiredSkill(
            Long teamId,
            Long skillId
    );

    TeamResponse removeRequiredSkill(
            Long teamId,
            Long skillId
    );

    List<SkillResponse> getRequiredSkills(
            Long teamId
    );

}