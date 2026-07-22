package com.teamup.teamup_backend.service.impl;

import com.teamup.teamup_backend.dto.response.UserSearchResponse;
import com.teamup.teamup_backend.mapper.UserMapper;
import com.teamup.teamup_backend.repository.SkillRepository;
import com.teamup.teamup_backend.repository.UserRepository;
import com.teamup.teamup_backend.service.SkillSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillSearchServiceImpl implements SkillSearchService {

    private final UserRepository userRepository;
    private final SkillRepository skillRepository;

    @Override
    public Page<UserSearchResponse> findUsersBySkill(
            Long skillId,
            Pageable pageable
    ) {

        if (skillId == null || !skillRepository.existsById(skillId)) {
            return Page.empty(pageable);
        }

        return userRepository
                .findDistinctUsersBySkillId(skillId, pageable)
                .map(UserMapper::toUserSearchResponse);
    }

    @Override
    public Page<UserSearchResponse> findUsersBySkills(
            List<Long> skillIds,
            Pageable pageable
    ) {

        if (skillIds == null || skillIds.isEmpty()) {
            return Page.empty(pageable);
        }

        List<Long> validSkillIds = skillIds.stream()
                .filter(skillRepository::existsById)
                .distinct()
                .toList();

        if (validSkillIds.isEmpty()) {
            return Page.empty(pageable);
        }

        return userRepository
                .findDistinctUsersBySkillIds(validSkillIds, pageable)
                .map(UserMapper::toUserSearchResponse);
    }
}