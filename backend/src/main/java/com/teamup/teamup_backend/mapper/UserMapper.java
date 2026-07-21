package com.teamup.teamup_backend.mapper;

import com.teamup.teamup_backend.dto.request.UpdateProfileRequest;
import com.teamup.teamup_backend.dto.response.MyProfileResponse;
import com.teamup.teamup_backend.dto.response.PublicProfileResponse;
import com.teamup.teamup_backend.dto.response.UserSearchResponse;
import com.teamup.teamup_backend.entity.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

public final class UserMapper {

    private UserMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

     // Convert User entity to MyProfileResponse.
    public static MyProfileResponse toMyProfileResponse(User user) {

        if (user == null) {
            return null;
        }

        return MyProfileResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .bio(user.getBio())
                .preferredRole(user.getPreferredRole())
                .githubUrl(user.getGithubUrl())
                .linkedinUrl(user.getLinkedinUrl())
                .whatsappNumber(user.getWhatsappNumber())
                .profileImageUrl(user.getProfileImageUrl())
                .emailVerified(user.getEmailVerified())
                .createdAt(user.getCreatedAt())
                .build();
    }

      //Convert User entity to PublicProfileResponse.
    public static PublicProfileResponse toPublicProfileResponse(User user) {

        if (user == null) {
            return null;
        }

        return PublicProfileResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .bio(user.getBio())
                .preferredRole(user.getPreferredRole())
                .githubUrl(user.getGithubUrl())
                .linkedinUrl(user.getLinkedinUrl())
                .profileImageUrl(user.getProfileImageUrl())
                .createdAt(user.getCreatedAt())
                .build();
    }

     //Update existing User entity from UpdateProfileRequest.
    public static void updateUserFromRequest(UpdateProfileRequest request, User user) {

        if (request == null || user == null) {
            return;
        }

        user.setFullName(request.getFullName());
        user.setBio(request.getBio());
        user.setPreferredRole(request.getPreferredRole());
        user.setGithubUrl(request.getGithubUrl());
        user.setLinkedinUrl(request.getLinkedinUrl());
        user.setWhatsappNumber(request.getWhatsappNumber());

        if (request.getAvatar() != null) {
            user.setProfileImageUrl(request.getAvatar().getFileName());
        }
    }

    public UserSearchResponse toUserSearchResponse(User user){

        if(user == null){
            return null;
        }

        return UserSearchResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .college(user.getCollege())
                .profileImageUrl(user.getProfileImageUrl())
                .bio(user.getBio())
                .build();
    }

    public List<UserSearchResponse> toUserSearchResponseList(List<User> users){

        if(users == null){
            return List.of();
        }

        return users.stream()
                .map(this::toUserSearchResponse)
                .toList();
    }

}