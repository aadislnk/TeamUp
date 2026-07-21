package com.teamup.teamup_backend.dto.response;

import com.teamup.teamup_backend.enums.PreferredRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicProfileResponse {

    private Long id;

    private String fullName;

    private String bio;

    private PreferredRole preferredRole;

    private String githubUrl;

    private String linkedinUrl;

    private String profileImageUrl;

    private LocalDateTime createdAt;

}
