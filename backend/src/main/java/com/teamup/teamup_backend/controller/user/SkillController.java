package com.teamup.teamup_backend.controller.user;

import com.teamup.teamup_backend.constant.ApiMessages;
import com.teamup.teamup_backend.constant.ApiPaths;
import com.teamup.teamup_backend.dto.common.ApiResponse;
import com.teamup.teamup_backend.dto.request.AddSkillsRequest;
import com.teamup.teamup_backend.dto.response.SkillResponse;
import com.teamup.teamup_backend.dto.response.UserSearchResponse;
import com.teamup.teamup_backend.dto.response.UserSkillResponse;
import com.teamup.teamup_backend.service.SkillSearchService;
import com.teamup.teamup_backend.service.SkillService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.API_BASE)
public class SkillController {

    private final SkillService skillService;
    private final SkillSearchService skillSearchService;

    public SkillController(
            SkillService skillService,
            SkillSearchService skillSearchService
    ) {
        this.skillService = skillService;
        this.skillSearchService = skillSearchService;
    }

      //GET /api/v1/skills
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

    // GET /api/v1/users/me/skills
    @GetMapping("/users/me/skills")
    public ResponseEntity<ApiResponse<List<UserSkillResponse>>> getMySkills() {

        List<UserSkillResponse> skills = skillService.getMySkills();

        return ResponseEntity.ok(
                ApiResponse.success(
                        ApiMessages.SKILLS_FETCHED,
                        skills
                )
        );
    }

    @PostMapping("/users/me/skills")
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

    @DeleteMapping("/users/me/skills/{skillId}")
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

    @GetMapping("/users/{userId}/skills")
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

    @GetMapping("/users/search")
    public ResponseEntity<ApiResponse<Page<UserSearchResponse>>> searchUsers(
            @RequestParam List<Long> skillIds,
            Pageable pageable) {

        Page<UserSearchResponse> users;

        if (skillIds.size() == 1) {
            users = skillSearchService.findUsersBySkill(
                    skillIds.get(0),
                    pageable
            );
        } else {
            users = skillSearchService.findUsersBySkills(
                    skillIds,
                    pageable
            );
        }

        return ResponseEntity.ok(
                ApiResponse.success(
                        ApiMessages.USERS_FETCHED,
                        users
                )
        );
    }
}