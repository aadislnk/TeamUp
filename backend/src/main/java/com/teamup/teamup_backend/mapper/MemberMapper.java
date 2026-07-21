package com.teamup.teamup_backend.mapper;


import com.teamup.teamup_backend.dto.response.MemberResponse;
import com.teamup.teamup_backend.entity.TeamMember;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MemberMapper {

    public MemberResponse toResponse(TeamMember teamMember) {

        return MemberResponse.builder()
                .userId(teamMember.getUser().getId())
                .fullName(teamMember.getUser().getFullName())
                .profileImageUrl(teamMember.getUser().getProfileImageUrl())
                .leader(
                        teamMember.getTeam()
                                .getLeader()
                                .getId()
                                .equals(teamMember.getUser().getId())
                )
                .build();
    }

    public List<MemberResponse> toResponseList(List<TeamMember> teamMembers) {

        return teamMembers.stream()
                .map(this::toResponse)
                .toList();
    }
}