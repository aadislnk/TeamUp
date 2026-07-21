package com.teamup.teamup_backend.service.impl;

import com.teamup.teamup_backend.constant.ApiMessages;
import com.teamup.teamup_backend.dto.request.ApplyJoinRequestRequest;
import com.teamup.teamup_backend.dto.response.IncomingJoinRequestResponse;
import com.teamup.teamup_backend.dto.response.JoinRequestResponse;
import com.teamup.teamup_backend.dto.response.OutgoingJoinRequestResponse;
import com.teamup.teamup_backend.entity.JoinRequest;
import com.teamup.teamup_backend.entity.Team;
import com.teamup.teamup_backend.entity.TeamMember;
import com.teamup.teamup_backend.entity.User;
import com.teamup.teamup_backend.enums.JoinRequestStatus;
import com.teamup.teamup_backend.enums.NotificationType;
import com.teamup.teamup_backend.exception.BadRequestException;
import com.teamup.teamup_backend.exception.ForbiddenException;
import com.teamup.teamup_backend.exception.ResourceNotFoundException;
import com.teamup.teamup_backend.mapper.JoinRequestMapper;
import com.teamup.teamup_backend.repository.JoinRequestRepository;
import com.teamup.teamup_backend.repository.TeamMemberRepository;
import com.teamup.teamup_backend.repository.TeamRepository;
import com.teamup.teamup_backend.service.CurrentUserService;
import com.teamup.teamup_backend.service.JoinRequestService;
import com.teamup.teamup_backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class JoinRequestServiceImpl implements JoinRequestService {

    private final JoinRequestRepository joinRequestRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final CurrentUserService currentUserService;
    private final JoinRequestMapper joinRequestMapper;
    private final NotificationService notificationService;

    @Override
    public JoinRequestResponse applyToTeam(
            Long teamId,
            ApplyJoinRequestRequest request
    ) {

        Team team = getTeamOrThrow(teamId);
        User currentUser = currentUserService.getCurrentUser();

        validateApplicantNotLeader(team, currentUser);
        validateRecruitmentOpen(team);
        validateCapacity(team);
        validateNotAlreadyMember(team, currentUser);
        validateNoDuplicateRequest(team, currentUser);
        validateNotInAnotherTeamForEvent(team, currentUser);

        JoinRequest joinRequest = JoinRequest.builder()
                .message(request.getMessage())
                .status(JoinRequestStatus.PENDING)
                .user(currentUser)
                .team(team)
                .build();

        JoinRequest savedRequest = joinRequestRepository.save(joinRequest);

        notificationService.createNotification(
                team.getLeader(),
                "New Join Request",
                currentUser.getFullName()
                        + " has submitted a request to join "
                        + team.getName()
                        + ".",
                NotificationType.JOIN_REQUEST_RECEIVED
        );

        notificationService.sendNotificationEmail(
                team.getLeader(),
                NotificationType.JOIN_REQUEST_RECEIVED,
                team.getLeader().getFullName(),
                currentUser.getFullName(),
                team.getName(),
                currentUser.getPreferredRole() == null
                        ? "Not specified"
                        : currentUser.getPreferredRole().toString()
        );

        return joinRequestMapper.toResponse(savedRequest);
    }

    @Override
    public void withdrawRequest(Long requestId) {

        JoinRequest joinRequest = getJoinRequestOrThrow(requestId);
        User currentUser = currentUserService.getCurrentUser();

        validateApplicantOwnership(joinRequest, currentUser);

        validatePendingRequest(joinRequest);

        Team team = joinRequest.getTeam();

        notificationService.createNotification(
                team.getLeader(),
                "Join Request Withdrawn",
                currentUser.getFullName()
                        + " has withdrawn the request to join "
                        + team.getName()
                        + ".",
                NotificationType.JOIN_REQUEST_WITHDRAWN
        );

        joinRequestRepository.delete(joinRequest);
    }

    @Override
    public JoinRequestResponse acceptRequest(Long requestId) {

        JoinRequest joinRequest = getJoinRequestOrThrow(requestId);
        Team team = joinRequest.getTeam();
        User applicant = joinRequest.getUser();

        validateLeaderOwnership(team);
        validatePendingRequest(joinRequest);
        validateRecruitmentOpen(team);
        validateCapacity(team);
        validateNotAlreadyMember(team, applicant);
        validateNotInAnotherTeamForEvent(team, applicant);

        TeamMember teamMember = TeamMember.builder()
                .team(team)
                .user(applicant)
                .joinedAt(LocalDateTime.now())
                .build();

        teamMemberRepository.save(teamMember);

        incrementTeamMembersAndCloseRecruitmentIfFull(team);
        teamRepository.save(team);

        joinRequest.setStatus(JoinRequestStatus.ACCEPTED);

        JoinRequest savedRequest = joinRequestRepository.save(joinRequest);

        notificationService.createNotification(
                applicant,
                "Join Request Accepted",
                "Your request to join "
                        + team.getName()
                        + " has been accepted.",
                NotificationType.JOIN_REQUEST_ACCEPTED
        );

        notificationService.sendNotificationEmail(
                applicant,
                NotificationType.JOIN_REQUEST_ACCEPTED,
                applicant.getFullName(),
                team.getName(),
                team.getLeader().getFullName()
        );

        return joinRequestMapper.toResponse(savedRequest);
    }

    @Override
    public JoinRequestResponse rejectRequest(Long requestId) {

        JoinRequest joinRequest = getJoinRequestOrThrow(requestId);

        validateLeaderOwnership(joinRequest.getTeam());
        validatePendingRequest(joinRequest);

        joinRequest.setStatus(JoinRequestStatus.REJECTED);
        JoinRequest savedRequest = joinRequestRepository.save(joinRequest);

        Team team = joinRequest.getTeam();
        User applicant = joinRequest.getUser();

        notificationService.createNotification(
                applicant,
                "Join Request Rejected",
                "Your request to join "
                        + team.getName()
                        + " has been rejected.",
                NotificationType.JOIN_REQUEST_REJECTED
        );

        notificationService.sendNotificationEmail(
                applicant,
                NotificationType.JOIN_REQUEST_REJECTED,
                applicant.getFullName(),
                team.getName(),
                team.getLeader().getFullName()
        );

        return joinRequestMapper.toResponse(savedRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncomingJoinRequestResponse> getIncomingRequests(Long teamId) {

        Team team = getTeamOrThrow(teamId);

        validateLeaderOwnership(team);

        List<JoinRequest> pendingRequests = joinRequestRepository.findByTeamAndStatus(
                team,
                JoinRequestStatus.PENDING
        );

        return joinRequestMapper.toIncomingResponseList(pendingRequests);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OutgoingJoinRequestResponse> getOutgoingRequests() {

        User currentUser = currentUserService.getCurrentUser();
        List<JoinRequest> requests = joinRequestRepository.findByUser(currentUser);

        return joinRequestMapper.toOutgoingResponseList(requests);
    }

    private Team getTeamOrThrow(Long teamId) {

        return teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ApiMessages.TEAM_NOT_FOUND
                ));
    }

    private JoinRequest getJoinRequestOrThrow(Long requestId) {

        return joinRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ApiMessages.JOIN_REQUEST_NOT_FOUND
                ));
    }

    private void validatePendingRequest(JoinRequest joinRequest) {

        if (joinRequest.getStatus() != JoinRequestStatus.PENDING) {
            throw new BadRequestException(ApiMessages.ONLY_PENDING_REQUEST_ALLOWED);
        }
    }

    private void validateLeaderOwnership(Team team) {

        User currentUser = currentUserService.getCurrentUser();

        if (!team.getLeader().getId().equals(currentUser.getId())) {
            throw new ForbiddenException(ApiMessages.ONLY_TEAM_LEADER_ALLOWED);
        }
    }

    private void validateRecruitmentOpen(Team team) {

        if (!Boolean.TRUE.equals(team.getRecruitmentOpen())) {
            throw new BadRequestException(ApiMessages.RECRUITMENT_CLOSED);
        }
    }

    private void validateCapacity(Team team) {

        if (team.getCurrentMembers() >= team.getMaxMembers()) {
            throw new BadRequestException(ApiMessages.TEAM_FULL);
        }
    }

    private void validateApplicantNotLeader(Team team, User applicant) {

        if (team.getLeader().getId().equals(applicant.getId())) {
            throw new BadRequestException(ApiMessages.TEAM_LEADER_CANNOT_APPLY);
        }
    }

    private void validateNotAlreadyMember(Team team, User user) {

        if (teamMemberRepository.existsByTeamAndUser(team, user)) {
            throw new BadRequestException(ApiMessages.APPLICANT_ALREADY_MEMBER);
        }
    }

    private void validateNoDuplicateRequest(Team team, User user) {

        if (joinRequestRepository.existsByTeamAndUserAndStatus(
                team,
                user,
                JoinRequestStatus.PENDING
        )) {
            throw new BadRequestException(ApiMessages.DUPLICATE_PENDING_REQUEST);
        }
    }

    private void validateNotInAnotherTeamForEvent(Team team, User user) {

        if (teamMemberRepository.existsByUserAndTeamEventId(
                user,
                team.getEvent().getId()
        )) {
            throw new BadRequestException(ApiMessages.ALREADY_IN_TEAM_FOR_EVENT);
        }
    }

    private void validateApplicantOwnership(JoinRequest joinRequest, User currentUser) {

        if (!joinRequest.getUser().getId().equals(currentUser.getId())) {
            throw new ForbiddenException(ApiMessages.ONLY_APPLICANT_CAN_WITHDRAW);
        }
    }

    private void incrementTeamMembersAndCloseRecruitmentIfFull(Team team) {

        team.setCurrentMembers(team.getCurrentMembers() + 1);

        if (team.getCurrentMembers().equals(team.getMaxMembers())) {
            team.setRecruitmentOpen(false);
        }
    }


}