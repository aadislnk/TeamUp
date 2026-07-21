package com.teamup.teamup_backend.service.impl;

import com.teamup.teamup_backend.constant.ApiMessages;
import com.teamup.teamup_backend.dto.request.CreateTeamRequest;
import com.teamup.teamup_backend.dto.request.TeamSearchRequest;
import com.teamup.teamup_backend.dto.request.UpdateTeamRequest;
import com.teamup.teamup_backend.dto.response.LeaderDashboardResponse;
import com.teamup.teamup_backend.dto.response.SkillResponse;
import com.teamup.teamup_backend.dto.response.TeamResponse;
import com.teamup.teamup_backend.entity.*;
import com.teamup.teamup_backend.enums.TeamStatus;
import com.teamup.teamup_backend.exception.BadRequestException;
import com.teamup.teamup_backend.exception.ForbiddenException;
import com.teamup.teamup_backend.exception.ResourceNotFoundException;
import com.teamup.teamup_backend.mapper.SkillMapper;
import com.teamup.teamup_backend.mapper.TeamMapper;
import com.teamup.teamup_backend.repository.*;
import com.teamup.teamup_backend.service.CurrentUserService;
import com.teamup.teamup_backend.service.TeamService;
import com.teamup.teamup_backend.specification.TeamSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.teamup.teamup_backend.dto.response.MemberResponse;
import com.teamup.teamup_backend.mapper.MemberMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final EventRepository eventRepository;
    private final CurrentUserService currentUserService;
    private final TeamMapper teamMapper;
    private final TeamSkillRepository teamSkillRepository;
    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;
    private final TeamMemberRepository teamMemberRepository;
    private final MemberMapper memberMapper;
    private final JoinRequestRepository joinRequestRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<TeamResponse> getAllTeams(Pageable pageable) {

        return teamRepository.findAll(pageable)
                .map(this::buildTeamResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public TeamResponse getTeamById(Long teamId) {

        return buildTeamResponse(
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

        return buildTeamResponse(savedTeam);
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

        return buildTeamResponse(updatedTeam);
    }

    @Override
    public void deleteTeam(Long teamId) {

        Team team = getTeamOrThrow(teamId);

        validateLeaderOwnership(team);

        validateTeamCanBeDeleted(team);

        teamRepository.delete(team);
    }
    @Override
    public TeamResponse updateRecruitmentStatus(
            Long teamId,
            Boolean recruitmentOpen
    ) {

        Team team = getTeamOrThrow(teamId);

        validateLeaderOwnership(team);

        if (Boolean.TRUE.equals(recruitmentOpen)) {
            validateRecruitmentCanBeOpened(team);
        }

        team.setRecruitmentOpen(recruitmentOpen);

        Team updatedTeam = teamRepository.save(team);

        return buildTeamResponse(updatedTeam);
    }

    @Override
    public TeamResponse updateWhatsAppGroupLink(
            Long teamId,
            String whatsappGroupLink
    ) {

        Team team = getTeamOrThrow(teamId);

        validateLeaderOwnership(team);

        team.setWhatsappGroupLink(whatsappGroupLink);

        Team updatedTeam = teamRepository.save(team);

        return buildTeamResponse(updatedTeam);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TeamResponse> getOpenTeams(Pageable pageable) {

        return teamRepository.findByRecruitmentOpenTrue(pageable)
                .map(this::buildTeamResponse);
    }
    @Override
    @Transactional(readOnly = true)
    public Page<TeamResponse> getTeamsByStatus(
            TeamStatus status,
            Pageable pageable
    ) {

        return teamRepository.findByStatus(status, pageable)
                .map(this::buildTeamResponse);
    }
    @Override
    @Transactional(readOnly = true)
    public Page<TeamResponse> getMyTeams(Pageable pageable) {

        User currentUser = currentUserService.getCurrentUser();

        return teamRepository.findByLeader(
                currentUser,
                pageable
        ).map(this::buildTeamResponse);
    }
    @Override
    public TeamResponse addRequiredSkill(
            Long teamId,
            Long skillId
    ) {

        Team team = getTeamOrThrow(teamId);

        validateLeaderOwnership(team);

        Skill skill = getSkillOrThrow(skillId);

        if (teamSkillRepository.existsByTeamAndSkill(team, skill)) {
            throw new BadRequestException(
                    ApiMessages.REQUIRED_SKILL_ALREADY_EXISTS
            );
        }

        TeamSkill teamSkill = TeamSkill.builder()
                .team(team)
                .skill(skill)
                .build();

        teamSkillRepository.save(teamSkill);

        return buildTeamResponse(team);
    }
    @Override
    public TeamResponse removeRequiredSkill(
            Long teamId,
            Long skillId
    ) {

        Team team = getTeamOrThrow(teamId);

        validateLeaderOwnership(team);

        Skill skill = getSkillOrThrow(skillId);

        TeamSkill teamSkill = teamSkillRepository
                .findByTeamAndSkill(team, skill)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                ApiMessages.REQUIRED_SKILL_NOT_FOUND
                        )
                );

        teamSkillRepository.delete(teamSkill);

        return buildTeamResponse(team);
    }
    @Override
    @Transactional(readOnly = true)
    public List<SkillResponse> getRequiredSkills(Long teamId) {

        Team team = getTeamOrThrow(teamId);

        return teamSkillRepository.findByTeam(team)
                .stream()
                .map(TeamSkill::getSkill)
                .map(skillMapper::toResponse)
                .toList();
    }
    @Override
    @Transactional(readOnly = true)
    public List<MemberResponse> getTeamMembers(Long teamId) {

        Team team = getTeamOrThrow(teamId);

        return teamMemberRepository.findByTeam(team)
                .stream()
                .map(memberMapper::toResponse)
                .toList();
    }
    @Override
    @Transactional(readOnly = true)
    public LeaderDashboardResponse getLeaderDashboard(Long teamId) {

        Team team = getTeamOrThrow(teamId);

        validateLeaderOwnership(team);

        return LeaderDashboardResponse.builder()
                .team(buildTeamResponse(team))
                .pendingRequestsCount(0) // temporary
                .acceptedMembers(team.getCurrentMembers())
                .recruitmentOpen(team.getRecruitmentOpen())
                .build();
    }
    @Override
    @Transactional(readOnly = true)
    public Page<TeamResponse> searchTeams(
            TeamSearchRequest request,
            Pageable pageable
    ) {

        Specification<Team> specification = Specification
                .where(TeamSpecification.hasKeyword(request.getKeyword()))
                .and(TeamSpecification.hasEvent(request.getEventId()))
                .and(TeamSpecification.hasStatus(request.getStatus()))
                .and(TeamSpecification.hasRecruitment(request.getRecruitmentOpen()))
                .and(TeamSpecification.hasRequiredSkill(request.getSkillId()));

        return teamRepository.findAll(specification, pageable)
                .map(this::buildTeamResponse);
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
    private void validateRecruitmentCanBeOpened(Team team) {

        validateTeamIsOpen(team);

        validateTeamCapacity(team);
    }
    private Skill getSkillOrThrow(Long skillId) {

        return skillRepository.findById(skillId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                ApiMessages.INVALID_SKILL
                        ));
    }
    private TeamResponse buildTeamResponse(Team team) {

        TeamResponse response = teamMapper.toResponse(team);

        response.setRequiredSkills(
                teamSkillRepository.findByTeam(team)
                        .stream()
                        .map(TeamSkill::getSkill)
                        .map(skillMapper::toResponse)
                        .toList()
        );

        response.setMembers(
                teamMemberRepository.findByTeam(team)
                        .stream()
                        .map(memberMapper::toResponse)
                        .toList()
        );

        return response;
    }

}
