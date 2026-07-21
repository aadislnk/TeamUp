package com.teamup.teamup_backend.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSkillResponse {

    private Long id;

    private SkillResponse skill;

}