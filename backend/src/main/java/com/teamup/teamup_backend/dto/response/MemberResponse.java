package com.teamup.teamup_backend.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {

    private Long userId;

    private String fullName;

    private String profileImageUrl;

    private Boolean leader;
}