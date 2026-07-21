package com.teamup.teamup_backend.dto.response;

import com.teamup.teamup_backend.enums.JoinRequestStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinRequestResponse {

    private Long requestId;

    private Long teamId;

    private String teamName;

    private Long userId;

    private String userName;

    private JoinRequestStatus status;

    private LocalDateTime createdAt;
}
