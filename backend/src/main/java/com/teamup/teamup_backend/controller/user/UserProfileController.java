package com.teamup.teamup_backend.controller.user;


import com.teamup.teamup_backend.constant.ApiMessages;
import com.teamup.teamup_backend.constant.ApiPaths;
import com.teamup.teamup_backend.dto.request.UpdateProfilePictureRequest;
import com.teamup.teamup_backend.dto.request.UpdateProfileRequest;
import com.teamup.teamup_backend.dto.common.ApiResponse;
import com.teamup.teamup_backend.dto.response.MyProfileResponse;
import com.teamup.teamup_backend.dto.response.PublicProfileResponse;
import com.teamup.teamup_backend.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPaths.USERS)
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;


    @GetMapping("/me")
    public ApiResponse<MyProfileResponse> getMyProfile() {

        return ApiResponse.success(
                ApiMessages.PROFILE_FETCHED,
                userProfileService.getMyProfile()
        );
    }


    @PutMapping("/me")
    public ApiResponse<MyProfileResponse> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request) {

        return ApiResponse.success(
                ApiMessages.PROFILE_UPDATED,
                userProfileService.updateProfile(request)
        );
    }


    @GetMapping("/{userId}")
    public ApiResponse<PublicProfileResponse> getPublicProfile(
            @PathVariable Long userId) {

        return ApiResponse.success(
                ApiMessages.PROFILE_FETCHED,
                userProfileService.getPublicProfile(userId)
        );
    }

    @PutMapping("/me/profile-picture")
    public ApiResponse<MyProfileResponse> updateProfilePicture(
            @Valid @RequestBody UpdateProfilePictureRequest request) {

        return ApiResponse.success(
                ApiMessages.PROFILE_AVATAR_UPDATED,
                userProfileService.updateProfilePicture(request)
        );
    }

    @DeleteMapping("/me/profile-picture")
    public ApiResponse<MyProfileResponse> removeProfilePicture() {

        return ApiResponse.success(
                ApiMessages.PROFILE_AVATAR_REMOVED,
                userProfileService.removeProfilePicture()
        );
    }
}