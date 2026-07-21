package com.teamup.teamup_backend.controller.team;

import com.teamup.teamup_backend.constant.ApiMessages;
import com.teamup.teamup_backend.constant.ApiPaths;
import com.teamup.teamup_backend.dto.common.ApiResponse;
import com.teamup.teamup_backend.dto.request.CreateTeamRequest;
import com.teamup.teamup_backend.dto.request.TeamSearchRequest;
import com.teamup.teamup_backend.dto.request.UpdateTeamRequest;
import com.teamup.teamup_backend.dto.response.TeamResponse;
import com.teamup.teamup_backend.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPaths.TEAMS)
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

//publics apis
    @GetMapping
    public ResponseEntity<ApiResponse<Page<TeamResponse>>> getAllTeams(
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        ApiMessages.SUCCESS,
                        teamService.getAllTeams(pageable)
                )
        );
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<ApiResponse<TeamResponse>> getTeamById(
            @PathVariable Long teamId
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        ApiMessages.SUCCESS,
                        teamService.getTeamById(teamId)
                )
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<TeamResponse>>> searchTeams(
            TeamSearchRequest request,
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        ApiMessages.SUCCESS,
                        teamService.searchTeams(request, pageable)
                )
        );
    }

//leader apis
    @PostMapping
    public ResponseEntity<ApiResponse<TeamResponse>> createTeam(
            @Valid @RequestBody CreateTeamRequest request
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                ApiMessages.TEAM_CREATED,
                                teamService.createTeam(request)
                        )
                );
    }

    @PutMapping("/{teamId}")
    public ResponseEntity<ApiResponse<TeamResponse>> updateTeam(
            @PathVariable Long teamId,
            @Valid @RequestBody UpdateTeamRequest request
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        ApiMessages.TEAM_UPDATED,
                        teamService.updateTeam(teamId, request)
                )
        );
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<ApiResponse<Void>> deleteTeam(
            @PathVariable Long teamId
    ) {

        teamService.deleteTeam(teamId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        ApiMessages.TEAM_DELETED,
                        null
                )
        );
    }

    @PatchMapping("/{teamId}/recruitment")
    public ResponseEntity<ApiResponse<TeamResponse>> updateRecruitmentStatus(
            @PathVariable Long teamId,
            @RequestParam Boolean recruitmentOpen
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        ApiMessages.RECRUITMENT_STATUS_UPDATED,
                        teamService.updateRecruitmentStatus(
                                teamId,
                                recruitmentOpen
                        )
                )
        );
    }

    @PatchMapping("/{teamId}/whatsapp")
    public ResponseEntity<ApiResponse<TeamResponse>> updateWhatsappGroupLink(
            @PathVariable Long teamId,
            @RequestParam String whatsappGroupLink
    ) {

        // TODO:
        // Add teamService.updateWhatsAppGroupLink(teamId, whatsappGroupLink)

        throw new UnsupportedOperationException(
                "WhatsApp group update not implemented yet."
        );
    }

    @PostMapping("/{teamId}/skills/{skillId}")
    public ResponseEntity<ApiResponse<TeamResponse>> addRequiredSkill(
            @PathVariable Long teamId,
            @PathVariable Long skillId
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        ApiMessages.REQUIRED_SKILL_ADDED,
                        teamService.addRequiredSkill(
                                teamId,
                                skillId
                        )
                )
        );
    }

    @DeleteMapping("/{teamId}/skills/{skillId}")
    public ResponseEntity<ApiResponse<TeamResponse>> removeRequiredSkill(
            @PathVariable Long teamId,
            @PathVariable Long skillId
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        ApiMessages.REQUIRED_SKILL_REMOVED,
                        teamService.removeRequiredSkill(
                                teamId,
                                skillId
                        )
                )
        );
    }
}
