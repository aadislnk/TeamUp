package com.teamup.teamup_backend.mapper;

import com.teamup.teamup_backend.dto.response.IncomingJoinRequestResponse;
import com.teamup.teamup_backend.dto.response.JoinRequestResponse;
import com.teamup.teamup_backend.dto.response.OutgoingJoinRequestResponse;
import com.teamup.teamup_backend.entity.JoinRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JoinRequestMapper {

    public JoinRequestResponse toResponse(JoinRequest joinRequest) {

        if (joinRequest == null) {
            return null;
        }

        return JoinRequestResponse.builder()
                .requestId(joinRequest.getId())
                .teamId(joinRequest.getTeam().getId())
                .teamName(joinRequest.getTeam().getName())
                .userId(joinRequest.getUser().getId())
                .userName(joinRequest.getUser().getFullName())
                .status(joinRequest.getStatus())
                .createdAt(joinRequest.getCreatedAt())
                .build();
    }

    public List<JoinRequestResponse> toResponseList(List<JoinRequest> joinRequests) {
        return joinRequests.stream()
                .map(this::toResponse)
                .toList();
    }

    public IncomingJoinRequestResponse toIncomingResponse(JoinRequest joinRequest) {

        if (joinRequest == null) {
            return null;
        }

        return IncomingJoinRequestResponse.builder()
                .requestId(joinRequest.getId())
                .userId(joinRequest.getUser().getId())
                .userName(joinRequest.getUser().getFullName())
                .profileImageUrl(joinRequest.getUser().getProfileImageUrl())
                .message(joinRequest.getMessage())
                .status(joinRequest.getStatus())
                .createdAt(joinRequest.getCreatedAt())
                .build();
    }

    public List<IncomingJoinRequestResponse> toIncomingResponseList(List<JoinRequest> joinRequests) {
        return joinRequests.stream()
                .map(this::toIncomingResponse)
                .toList();
    }

    public OutgoingJoinRequestResponse toOutgoingResponse(JoinRequest joinRequest) {

        if (joinRequest == null) {
            return null;
        }

        return OutgoingJoinRequestResponse.builder()
                .requestId(joinRequest.getId())
                .teamId(joinRequest.getTeam().getId())
                .teamName(joinRequest.getTeam().getName())
                .status(joinRequest.getStatus())
                .createdAt(joinRequest.getCreatedAt())
                .build();
    }

    public List<OutgoingJoinRequestResponse> toOutgoingResponseList(List<JoinRequest> joinRequests) {
        return joinRequests.stream()
                .map(this::toOutgoingResponse)
                .toList();
    }
}