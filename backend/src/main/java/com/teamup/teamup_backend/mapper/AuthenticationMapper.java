package com.teamup.teamup_backend.mapper;

import com.teamup.teamup_backend.constant.ApiMessages;
import com.teamup.teamup_backend.dto.request.RegisterRequest;
import com.teamup.teamup_backend.dto.response.LoginResponse;
import com.teamup.teamup_backend.dto.response.RegisterResponse;
import com.teamup.teamup_backend.entity.User;
import com.teamup.teamup_backend.enums.Avatar;
import com.teamup.teamup_backend.enums.Role;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationMapper {

    public User toUser(RegisterRequest request) {
        return User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(request.getPassword())
                .college(request.getCollege())
                .academicYear(request.getAcademicYear())
                .gender(request.getGender())
                .role(Role.USER)
                .profileImageUrl(Avatar.DEFAULT.getFileName())
                .emailVerified(false)
                .build();
    }

    public RegisterResponse toRegisterResponse(User user) {
        return RegisterResponse.builder()
                .email(user.getEmail())
                .message(ApiMessages.REGISTRATION_SUCCESS)
                .verificationRequired(true)
                .build();
    }

    public LoginResponse toLoginResponse(
            User user,
            String accessToken,
            Long expiresIn
    ) {

        return LoginResponse.builder()
                .accessToken(accessToken)
                .expiresIn(expiresIn)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .build();
    }

}