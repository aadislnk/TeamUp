package com.teamup.teamup_backend.service;

import com.teamup.teamup_backend.dto.request.ApplyJoinRequestRequest;
import com.teamup.teamup_backend.dto.response.IncomingJoinRequestResponse;
import com.teamup.teamup_backend.dto.response.JoinRequestResponse;
import com.teamup.teamup_backend.dto.response.OutgoingJoinRequestResponse;

import java.util.List;

public interface JoinRequestService {

    JoinRequestResponse applyToTeam(
            Long teamId,
            ApplyJoinRequestRequest request
    );

    void withdrawRequest(
            Long requestId
    );

    JoinRequestResponse acceptRequest(
            Long requestId
    );

    JoinRequestResponse rejectRequest(
            Long requestId
    );

    List<IncomingJoinRequestResponse> getIncomingRequests(
            Long teamId
    );

    List<OutgoingJoinRequestResponse> getOutgoingRequests();

}
