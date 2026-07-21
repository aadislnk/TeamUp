package com.teamup.teamup_backend.service.impl;

import com.teamup.teamup_backend.constant.ApiMessages;
import com.teamup.teamup_backend.dto.request.CreateTeamRequest;
import com.teamup.teamup_backend.dto.request.UpdateTeamRequest;
import com.teamup.teamup_backend.dto.response.TeamResponse;
import com.teamup.teamup_backend.entity.Event;
import com.teamup.teamup_backend.entity.Team;
import com.teamup.teamup_backend.entity.User;
import com.teamup.teamup_backend.enums.TeamStatus;
import com.teamup.teamup_backend.exception.BadRequestException;
import com.teamup.teamup_backend.exception.ForbiddenException;
import com.teamup.teamup_backend.exception.ResourceNotFoundException;
import com.teamup.teamup_backend.mapper.TeamMapper;
import com.teamup.teamup_backend.repository.EventRepository;
import com.teamup.teamup_backend.repository.TeamRepository;
import com.teamup.teamup_backend.service.CurrentUserService;
import com.teamup.teamup_backend.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final EventRepository eventRepository;
    private final CurrentUserService currentUserService;
    private final TeamMapper teamMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<TeamResponse> getAllTeams(Pageable pageable) {

        return teamRepository.findAll(pageable)
                .map(teamMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public TeamResponse getTeamById(Long teamId) {

        return teamMapper.toResponse(
                getTeamOrThrow(teamId)
        );
    }

    @Override
    public TeamResponse createTeam(CreateTeamRequest request) {

        Event event = getEventOrThrow(request.getEventId());

        validateDuplicateTeam(
                request.getName(),
                event
        );

        User leader = currentUserService.getCurrentUser();

        validateLeaderCanCreateTeam(
                leader,
                event
        );

        Team team = teamMapper.createEntity(
                request,
                event,
                leader
        );

        Team savedTeam = teamRepository.save(team);

        return teamMapper.toResponse(savedTeam);
    }

    @Override
    public TeamResponse updateTeam(
            Long teamId,
            UpdateTeamRequest request
    ) {

        Team team = getTeamOrThrow(teamId);

        validateLeaderOwnership(team);

        validateDuplicateTeamForUpdate(
                request.getName(),
                team.getEvent(),
                teamId
        );

        validateMaxMembers(
                team,
                request.getMaxMembers()
        );

        teamMapper.updateEntity(team, request);

        Team updatedTeam = teamRepository.save(team);

        return teamMapper.toResponse(updatedTeam);
    }

    @Override
    public void deleteTeam(Long teamId) {

        Team team = getTeamOrThrow(teamId);

        validateLeaderOwnership(team);

        validateTeamCanBeDeleted(team);

        teamRepository.delete(team);
    }
    @Override
    @Transactional(readOnly = true)
    public Page<TeamResponse> getOpenTeams(Pageable pageable) {

        return teamRepository.findByRecruitmentOpenTrue(pageable)
                .map(teamMapper::toResponse);
    }
    @Override
    @Transactional(readOnly = true)
    public Page<TeamResponse> getTeamsByStatus(
            TeamStatus status,
            Pageable pageable
    ) {

        return teamRepository.findByStatus(status, pageable)
                .map(teamMapper::toResponse);
    }
    @Override
    @Transactional(readOnly = true)
    public Page<TeamResponse> getMyTeams(Pageable pageable) {

        User currentUser = currentUserService.getCurrentUser();

        return teamRepository.findByLeader(
                currentUser,
                pageable
        ).map(teamMapper::toResponse);
    }

//helpers
private Team getTeamOrThrow(Long id) {

    return teamRepository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            ApiMessages.TEAM_NOT_FOUND
                    ));
}

    private Event getEventOrThrow(Long id) {

        return eventRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                ApiMessages.EVENT_NOT_FOUND
                        ));
    }

    private void validateDuplicateTeam(
            String name,
            Event event
    ) {

        if (teamRepository.existsByNameIgnoreCaseAndEvent(
                name,
                event
        )) {
            throw new BadRequestException(
                    ApiMessages.TEAM_ALREADY_EXISTS
            );
        }
    }

    private void validateDuplicateTeamForUpdate(
            String name,
            Event event,
            Long teamId
    ) {

        if (teamRepository.existsByNameIgnoreCaseAndEventAndIdNot(
                name,
                event,
                teamId
        )) {
            throw new BadRequestException(
                    ApiMessages.TEAM_ALREADY_EXISTS
            );
        }
    }

    private void validateMaxMembers(
            Team team,
            Integer maxMembers
    ) {

        if (maxMembers < team.getCurrentMembers()) {
            throw new BadRequestException(
                    ApiMessages.MAX_MEMBERS_LESS_THAN_CURRENT
            );
        }
    }

    private void validateLeaderCanCreateTeam(
            User leader,
            Event event
    ) {

        if (teamRepository.existsByLeaderAndEvent(leader, event)) {
            throw new BadRequestException(
                    ApiMessages.ALREADY_LEADING_TEAM_FOR_EVENT
            );
        }
    }

    private void validateLeaderOwnership(Team team) {

        User currentUser = currentUserService.getCurrentUser();

        if (!team.getLeader().getId().equals(currentUser.getId())) {
            throw new ForbiddenException(
                    ApiMessages.ONLY_TEAM_LEADER_ALLOWED
            );
        }
    }
    private void validateTeamCanBeDeleted(Team team) {

        if (team.getCurrentMembers() > 1) {
            throw new BadRequestException(
                    ApiMessages.CANNOT_DELETE_TEAM_WITH_MEMBERS
            );
        }
    }
    private void validateTeamIsOpen(Team team) {

        if (team.getStatus() != TeamStatus.OPEN) {
            throw new BadRequestException(
                    ApiMessages.TEAM_NOT_OPEN
            );
        }
    }
    private void validateRecruitmentOpen(Team team) {

        if (!Boolean.TRUE.equals(team.getRecruitmentOpen())) {
            throw new BadRequestException(
                    ApiMessages.RECRUITMENT_CLOSED
            );
        }
    }
    private void validateTeamCapacity(Team team) {

        if (team.getCurrentMembers() >= team.getMaxMembers()) {
            throw new BadRequestException(
                    ApiMessages.TEAM_FULL
            );
        }
    }

}
