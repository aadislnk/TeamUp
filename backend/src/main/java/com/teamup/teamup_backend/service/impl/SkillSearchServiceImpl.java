package com.teamup.teamup_backend.service.impl;

import com.teamup.teamup_backend.dto.response.UserSearchResponse;
import com.teamup.teamup_backend.mapper.UserMapper;
import com.teamup.teamup_backend.repository.SkillRepository;
import com.teamup.teamup_backend.repository.UserRepository;
import com.teamup.teamup_backend.service.SkillSearchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillSearchServiceImpl implements SkillSearchService {

    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final UserMapper userMapper;

    public SkillSearchServiceImpl(
            UserRepository userRepository,
            SkillRepository skillRepository,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Page<UserSearchResponse> findUsersBySkill(
            Long skillId,
            Pageable pageable
    ) {

        if(skillId == null || !skillRepository.existsById(skillId)){
            return Page.empty(pageable);
        }

        return userRepository
                .findDistinctUsersBySkillId(skillId,pageable)
                .map(userMapper::toUserSearchResponse);
    }

    @Override
    public Page<UserSearchResponse> findUsersBySkills(
            List<Long> skillIds,
            Pageable pageable
    ) {

        if(skillIds == null || skillIds.isEmpty()){
            return Page.empty();
        }

        List<Long> validSkillIds = skillIds.stream()
                .filter(skillRepository::existsById)
                .distinct()
                .toList();

        if(validSkillIds.isEmpty()){
            return Page.empty(pageable);
        }

        return userRepository
                .findDistinctUsersBySkillIds(validSkillIds,pageable)
                .map(userMapper::toUserSearchResponse);
    }
}