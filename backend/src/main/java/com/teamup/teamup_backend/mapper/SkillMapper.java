package com.teamup.teamup_backend.mapper;

import com.teamup.teamup_backend.dto.response.SkillResponse;
import com.teamup.teamup_backend.dto.response.UserSkillResponse;
import com.teamup.teamup_backend.entity.Skill;
import com.teamup.teamup_backend.entity.UserSkill;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public final class SkillMapper {

    public SkillResponse toResponse(Skill skill) {

        if (skill == null) {
            return null;
        }

        return SkillResponse.builder()
                .id(skill.getId())
                .name(skill.getName())
                .build();
    }

    public UserSkillResponse toUserSkillResponse(UserSkill userSkill) {

        if (userSkill == null) {
            return null;
        }

        return UserSkillResponse.builder()
                .id(userSkill.getId())
                .skill(toResponse(userSkill.getSkill()))
                .build();
    }

    public List<SkillResponse> toResponseList(List<Skill> skills) {

        if (skills == null) {
            return List.of();
        }

        return skills.stream()
                .map(this::toResponse)
                .toList();
    }

    public List<UserSkillResponse> toUserSkillResponseList(List<UserSkill> userSkills) {

        if (userSkills == null) {
            return List.of();
        }

        return userSkills.stream()
                .map(this::toUserSkillResponse)
                .toList();
    }
}