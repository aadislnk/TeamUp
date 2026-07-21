package com.teamup.teamup_backend.mapper;

import com.teamup.teamup_backend.dto.response.SkillResponse;
import com.teamup.teamup_backend.dto.response.UserSkillResponse;
import com.teamup.teamup_backend.entity.Skill;
import com.teamup.teamup_backend.entity.UserSkill;
import java.util.List;

public final class SkillMapper {

    private SkillMapper() {
    }

    public static SkillResponse toSkillResponse(Skill skill) {

        if (skill == null) {
            return null;
        }

        return SkillResponse.builder()
                .id(skill.getId())
                .name(skill.getName())
                .build();
    }

    public static UserSkillResponse toUserSkillResponse(UserSkill userSkill) {

        if (userSkill == null) {
            return null;
        }

        return UserSkillResponse.builder()
                .id(userSkill.getId())
                .skill(toSkillResponse(userSkill.getSkill()))
                .build();
    }

    public static List<SkillResponse> toSkillResponseList(List<Skill> skills) {

        return skills.stream()
                .map(SkillMapper::toSkillResponse)
                .toList();
    }

    public static List<UserSkillResponse> toUserSkillResponseList(List<UserSkill> userSkills) {

        return userSkills.stream()
                .map(SkillMapper::toUserSkillResponse)
                .toList();
    }

}
