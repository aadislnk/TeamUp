package com.teamup.teamup_backend.controller.user;

import com.teamup.teamup_backend.constant.ApiMessages;
import com.teamup.teamup_backend.constant.ApiPaths;
import com.teamup.teamup_backend.dto.request.AddSkillsRequest;
import com.teamup.teamup_backend.dto.common.ApiResponse;
import com.teamup.teamup_backend.dto.response.SkillResponse;
import com.teamup.teamup_backend.dto.response.UserSkillResponse;
import com.teamup.teamup_backend.service.SkillService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.API_BASE)
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping("/skills")
    public ResponseEntity<ApiResponse<List<SkillResponse>>> getAllSkills() {

        List<SkillResponse> skills = skillService.getAllSkills();

        return ResponseEntity.ok(
                ApiResponse.success(
                        ApiMessages.SKILLS_FETCHED,
                        skills
                )
        );
    }

    @GetMapping(ApiPaths.USERS + ApiPaths.CURRENT_USER + ApiPaths.USER_SKILLS)
    public ResponseEntity<ApiResponse<List<UserSkillResponse>>> getMySkills() {

        List<UserSkillResponse> skills = skillService.getMySkills();

        return ResponseEntity.ok(
                ApiResponse.success(
                        ApiMessages.SKILLS_FETCHED,
                        skills
                )
        );
    }

    @PostMapping(ApiPaths.USERS + ApiPaths.CURRENT_USER + ApiPaths.USER_SKILLS)
    public ResponseEntity<ApiResponse<List<UserSkillResponse>>> addSkills(
            @Valid @RequestBody AddSkillsRequest request) {

        List<UserSkillResponse> skills = skillService.addSkills(request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        ApiMessages.SKILLS_ADDED,
                        skills
                )
        );
    }

    @DeleteMapping(ApiPaths.USERS + ApiPaths.CURRENT_USER + ApiPaths.USER_SKILLS + ApiPaths.SKILL_ID)
    public ResponseEntity<ApiResponse<Void>> removeSkill(
            @PathVariable Long skillId) {

        skillService.removeSkill(skillId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        ApiMessages.SKILL_REMOVED,
                        null
                )
        );
    }

    @GetMapping(ApiPaths.USERS + ApiPaths.USER_ID + ApiPaths.USER_SKILLS)
    public ResponseEntity<ApiResponse<List<UserSkillResponse>>> getUserSkills(
            @PathVariable Long userId) {

        List<UserSkillResponse> skills = skillService.getUserSkills(userId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        ApiMessages.SKILLS_FETCHED,
                        skills
                )
        );
    }
}