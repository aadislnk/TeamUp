package com.teamup.teamup_backend.service.impl;

import com.teamup.teamup_backend.constant.ApiMessages;
import com.teamup.teamup_backend.dto.request.UpdateProfilePictureRequest;
import com.teamup.teamup_backend.dto.request.UpdateProfileRequest;
import com.teamup.teamup_backend.dto.response.MyProfileResponse;
import com.teamup.teamup_backend.dto.response.PublicProfileResponse;
import com.teamup.teamup_backend.entity.User;
import com.teamup.teamup_backend.enums.Avatar;
import com.teamup.teamup_backend.exception.UserNotFoundException;
import com.teamup.teamup_backend.mapper.UserMapper;
import com.teamup.teamup_backend.repository.UserRepository;
import com.teamup.teamup_backend.security.model.CustomUserDetails;
import com.teamup.teamup_backend.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public MyProfileResponse getMyProfile() {

        User user = getCurrentAuthenticatedUser();

        return UserMapper.toMyProfileResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public PublicProfileResponse getPublicProfile(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(ApiMessages.RESOURCE_NOT_FOUND));

        return UserMapper.toPublicProfileResponse(user);
    }

    @Override
    public MyProfileResponse updateProfile(UpdateProfileRequest request) {

        User user = getCurrentAuthenticatedUser();

        user.setFullName(normalize(request.getFullName()));

        user.setBio(updateOptionalField(request.getBio(), user.getBio()));

        user.setGithubUrl(updateOptionalField(request.getGithubUrl(), user.getGithubUrl()));

        user.setLinkedinUrl(updateOptionalField(request.getLinkedinUrl(), user.getLinkedinUrl()));

        user.setWhatsappNumber(updateOptionalField(request.getWhatsappNumber(), user.getWhatsappNumber()));

        user.setPreferredRole(request.getPreferredRole());

        if (request.getAvatar() != null) {
            user.setProfileImageUrl(request.getAvatar().getFileName());
        }

        userRepository.save(user);

        return UserMapper.toMyProfileResponse(user);
    }

    @Override
    public MyProfileResponse updateProfilePicture(UpdateProfilePictureRequest request) {

        User user = getCurrentAuthenticatedUser();

        user.setProfileImageUrl(request.getAvatar().getFileName());

        userRepository.save(user);

        return UserMapper.toMyProfileResponse(user);
    }

    @Override
    public MyProfileResponse removeProfilePicture() {

        User user = getCurrentAuthenticatedUser();

        user.setProfileImageUrl(Avatar.DEFAULT.getFileName());

        userRepository.save(user);

        return UserMapper.toMyProfileResponse(user);
    }

    //returns the currently authenticated user.
    private User getCurrentAuthenticatedUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotFoundException(ApiMessages.UNAUTHORIZED);
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof CustomUserDetails customUserDetails)) {
            throw new UserNotFoundException(ApiMessages.UNAUTHORIZED);
        }

        return customUserDetails.getUser();
    }

    private String normalize(String value) {

        if (value == null) {
            return null;
        }

        String trimmed = value.trim();

        return trimmed.isEmpty() ? null : trimmed;
    }

    private String updateOptionalField(String newValue, String currentValue) {

        if (newValue == null) {
            return currentValue;
        }

        String trimmed = newValue.trim();

        return trimmed.isEmpty() ? currentValue : trimmed;
    }
}