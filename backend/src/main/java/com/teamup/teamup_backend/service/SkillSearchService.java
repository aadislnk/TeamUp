package com.teamup.teamup_backend.service;

import com.teamup.teamup_backend.dto.response.UserSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SkillSearchService {

    Page<UserSearchResponse> findUsersBySkill(
            Long skillId,
            Pageable pageable
    );

    Page<UserSearchResponse> findUsersBySkills(
            List<Long> skillIds,
            Pageable pageable
    );
}
