package com.teamup.teamup_backend.service;

import com.teamup.teamup_backend.dto.request.UpdateProfilePictureRequest;
import com.teamup.teamup_backend.dto.request.UpdateProfileRequest;
import com.teamup.teamup_backend.dto.response.MyProfileResponse;
import com.teamup.teamup_backend.dto.response.PublicProfileResponse;

public interface UserProfileService {

    MyProfileResponse getMyProfile();

    PublicProfileResponse getPublicProfile(Long userId);

    MyProfileResponse updateProfile(UpdateProfileRequest request);

    MyProfileResponse updateProfilePicture(UpdateProfilePictureRequest request);

    MyProfileResponse removeProfilePicture();
}