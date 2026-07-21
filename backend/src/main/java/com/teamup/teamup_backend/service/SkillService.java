package com.teamup.teamup_backend.service;

import com.teamup.teamup_backend.dto.request.AddSkillsRequest;
import com.teamup.teamup_backend.dto.response.SkillResponse;
import com.teamup.teamup_backend.dto.response.UserSkillResponse;

import java.util.List;

public interface SkillService {
    List<SkillResponse> getAllSkills();
    List<UserSkillResponse> getMySkills();
    List<UserSkillResponse> getUserSkills(Long userId);
    List<UserSkillResponse> addSkills(AddSkillsRequest request);
    void removeSkill(Long skillId);

}