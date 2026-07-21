package com.teamup.teamup_backend.dto.response;

import com.teamup.teamup_backend.enums.JoinRequestStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncomingJoinRequestResponse {

    private Long requestId;

    private Long userId;

    private String userName;

    private String profileImageUrl;

    private String message;

    private JoinRequestStatus status;

    private LocalDateTime createdAt;
}