package com.teamup.teamup_backend.dto.response;

import com.teamup.teamup_backend.enums.JoinRequestStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutgoingJoinRequestResponse {

    private Long requestId;

    private Long teamId;

    private String teamName;

    private JoinRequestStatus status;

    private LocalDateTime createdAt;
}