package com.teamup.teamup_backend.controller.joinrequest;


import com.teamup.teamup_backend.constant.ApiMessages;
import com.teamup.teamup_backend.constant.ApiPaths;
import com.teamup.teamup_backend.dto.request.ApplyJoinRequestRequest;
import com.teamup.teamup_backend.dto.common.ApiResponse;
import com.teamup.teamup_backend.dto.response.IncomingJoinRequestResponse;
import com.teamup.teamup_backend.dto.response.JoinRequestResponse;
import com.teamup.teamup_backend.dto.response.OutgoingJoinRequestResponse;
import com.teamup.teamup_backend.service.JoinRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPaths.API_BASE)
public class JoinRequestController {

    private final JoinRequestService joinRequestService;

    @PostMapping(ApiPaths.TEAMS + "/{teamId}/apply")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<JoinRequestResponse> applyToTeam(
            @PathVariable Long teamId,
            @Valid @RequestBody ApplyJoinRequestRequest request
    ) {

        JoinRequestResponse response = joinRequestService.applyToTeam(
                teamId,
                request
        );

        return ApiResponse.success(
                ApiMessages.JOIN_REQUEST_CREATED,
                response
        );
    }

    @DeleteMapping(ApiPaths.REQUESTS + "/{requestId}")
    public ApiResponse<Void> withdrawRequest(
            @PathVariable Long requestId
    ) {

        joinRequestService.withdrawRequest(requestId);

        return ApiResponse.success(
                ApiMessages.JOIN_REQUEST_WITHDRAWN,
                null
        );
    }

    @PatchMapping(ApiPaths.REQUESTS + "/{requestId}/accept")
    public ApiResponse<JoinRequestResponse> acceptRequest(
            @PathVariable Long requestId
    ) {

        JoinRequestResponse response =
                joinRequestService.acceptRequest(requestId);

        return ApiResponse.success(
                ApiMessages.JOIN_REQUEST_ACCEPTED,
                response
        );
    }

    @PatchMapping(ApiPaths.REQUESTS + "/{requestId}/reject")
    public ApiResponse<JoinRequestResponse> rejectRequest(
            @PathVariable Long requestId
    ) {

        JoinRequestResponse response =
                joinRequestService.rejectRequest(requestId);

        return ApiResponse.success(
                ApiMessages.JOIN_REQUEST_REJECTED,
                response
        );
    }

    @GetMapping(ApiPaths.TEAMS + "/{teamId}/requests")
    public ApiResponse<List<IncomingJoinRequestResponse>> getIncomingRequests(
            @PathVariable Long teamId
    ) {

        List<IncomingJoinRequestResponse> response =
                joinRequestService.getIncomingRequests(teamId);

        return ApiResponse.success(
                ApiMessages.INCOMING_JOIN_REQUESTS_FETCHED,
                response
        );
    }

    @GetMapping(ApiPaths.USERS + "/me/requests")
    public ApiResponse<List<OutgoingJoinRequestResponse>> getOutgoingRequests() {

        List<OutgoingJoinRequestResponse> response =
                joinRequestService.getOutgoingRequests();

        return ApiResponse.success(
                ApiMessages.OUTGOING_JOIN_REQUESTS_FETCHED,
                response
        );
    }

}
