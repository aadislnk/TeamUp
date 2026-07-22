package com.teamup.teamup_backend.service.impl;

import com.teamup.teamup_backend.constant.ApiMessages;
import com.teamup.teamup_backend.constant.ValidationMessages;
import com.teamup.teamup_backend.dto.request.AddSkillsRequest;
import com.teamup.teamup_backend.dto.response.SkillResponse;
import com.teamup.teamup_backend.dto.response.UserSkillResponse;
import com.teamup.teamup_backend.entity.Skill;
import com.teamup.teamup_backend.entity.User;
import com.teamup.teamup_backend.entity.UserSkill;
import com.teamup.teamup_backend.exception.BadRequestException;
import com.teamup.teamup_backend.exception.ResourceNotFoundException;
import com.teamup.teamup_backend.exception.UnauthorizedException;
import com.teamup.teamup_backend.mapper.SkillMapper;
import com.teamup.teamup_backend.repository.SkillRepository;
import com.teamup.teamup_backend.repository.UserRepository;
import com.teamup.teamup_backend.repository.UserSkillRepository;
import com.teamup.teamup_backend.security.model.CustomUserDetails;
import com.teamup.teamup_backend.service.SkillService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;
    private final UserRepository userRepository;
    private final UserSkillRepository userSkillRepository;
    private final SkillMapper skillMapper;

    public SkillServiceImpl(
            SkillRepository skillRepository,
            UserRepository userRepository,
            UserSkillRepository userSkillRepository,
            SkillMapper skillMapper
    ) {
        this.skillRepository = skillRepository;
        this.userRepository = userRepository;
        this.userSkillRepository = userSkillRepository;
        this.skillMapper = skillMapper;
    }

    @Override
    public List<SkillResponse> getAllSkills() {

        List<Skill> skills = skillRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));

        return skillMapper.toResponseList(skills);
    }

    @Override
    public List<UserSkillResponse> getMySkills() {

        User user = getAuthenticatedUser();

        List<UserSkill> userSkills = userSkillRepository.findByUser(user);

        return skillMapper.toUserSkillResponseList(userSkills);
    }

    @Override
    public List<UserSkillResponse> getUserSkills(Long userId) {

        if (userId == null || userId <= 0) {
            throw new BadRequestException(ValidationMessages.INVALID_VALUE);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(ApiMessages.RESOURCE_NOT_FOUND));

        List<UserSkill> userSkills = userSkillRepository.findByUser(user);

        return skillMapper.toUserSkillResponseList(userSkills);
    }

    @Override
    public List<UserSkillResponse> addSkills(AddSkillsRequest request) {

        if (request == null || request.getSkillIds() == null || request.getSkillIds().isEmpty()) {
            throw new BadRequestException(ValidationMessages.REQUIRED_FIELD);
        }

        User user = getAuthenticatedUser();

        Set<Long> requestedSkillIds = new LinkedHashSet<>(request.getSkillIds());

        Set<Long> existingSkillIds = userSkillRepository.findByUser(user)
                .stream()
                .map(userSkill -> userSkill.getSkill().getId())
                .collect(Collectors.toSet());

        for (Long skillId : requestedSkillIds) {

            if (skillId == null || skillId <= 0) {
                throw new BadRequestException(ValidationMessages.INVALID_VALUE);
            }

            Skill skill = skillRepository.findById(skillId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(ApiMessages.RESOURCE_NOT_FOUND));

            if (!existingSkillIds.contains(skillId)) {

                UserSkill userSkill = UserSkill.builder()
                        .user(user)
                        .skill(skill)
                        .build();

                userSkillRepository.save(userSkill);
            }
        }

        List<UserSkill> updatedSkills = userSkillRepository.findByUser(user);

        return skillMapper.toUserSkillResponseList(updatedSkills);
    }

    @Override
    public void removeSkill(Long skillId) {

        if (skillId == null || skillId <= 0) {
            throw new BadRequestException(ValidationMessages.INVALID_VALUE);
        }

        User user = getAuthenticatedUser();

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(ApiMessages.RESOURCE_NOT_FOUND));

        if (!userSkillRepository.existsByUserAndSkill(user, skill)) {
            throw new ResourceNotFoundException(ApiMessages.RESOURCE_NOT_FOUND);
        }

        userSkillRepository.deleteByUserAndSkill(user, skill);
    }

    private User getAuthenticatedUser() {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null ||
                !(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {

            throw new UnauthorizedException(ApiMessages.UNAUTHORIZED);
        }

        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() ->
                        new UnauthorizedException(ApiMessages.UNAUTHORIZED));
    }
}