package com.teamup.teamup_backend.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSearchResponse {

    private Long id;

    private String fullName;

    private String college;

    private String profileImageUrl;

    private String bio;
}