package com.teamup.teamup_backend.service.impl;

import com.teamup.teamup_backend.constant.ApiMessages;
import com.teamup.teamup_backend.dto.request.LoginRequest;
import com.teamup.teamup_backend.dto.request.RegisterRequest;
import com.teamup.teamup_backend.dto.response.LoginResponse;
import com.teamup.teamup_backend.dto.response.RegisterResponse;
import com.teamup.teamup_backend.entity.User;
import com.teamup.teamup_backend.exception.BadRequestException;
import com.teamup.teamup_backend.exception.ConflictException;
import com.teamup.teamup_backend.exception.ForbiddenException;
import com.teamup.teamup_backend.exception.UnauthorizedException;
import com.teamup.teamup_backend.mapper.AuthenticationMapper;
import com.teamup.teamup_backend.repository.UserRepository;
import com.teamup.teamup_backend.security.jwt.JwtService;
import com.teamup.teamup_backend.security.model.CustomUserDetails;
import com.teamup.teamup_backend.service.AuthenticationService;
import com.teamup.teamup_backend.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationMapper authenticationMapper;
    private final EmailVerificationService emailVerificationService;
    private final JwtService  jwtService;

    @Override
    public RegisterResponse register(RegisterRequest request) {

        validatePasswords(request);

        User user = userRepository.findByEmail(request.getEmail())
                .map(existingUser -> {

                    if (Boolean.TRUE.equals(existingUser.getEmailVerified())) {
                        throw new ConflictException(ApiMessages.EMAIL_ALREADY_REGISTERED);
                    }

                    return updateExistingUser(existingUser, request);
                })
                .orElseGet(() -> createNewUser(request));

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        emailVerificationService.sendVerificationOtp(savedUser.getEmail());

        return buildRegisterResponse(savedUser);
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new UnauthorizedException(ApiMessages.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException(ApiMessages.INVALID_CREDENTIALS);
        }

        if (!Boolean.TRUE.equals(user.getEmailVerified())) {
            throw new ForbiddenException(ApiMessages.EMAIL_NOT_VERIFIED);
        }


        String accessToken =
                jwtService.generateToken(user);

        long expiresIn =
                jwtService.getExpiration();

        return authenticationMapper.toLoginResponse(
                user,
                accessToken,
                expiresIn
        );
    }

    private void validatePasswords(RegisterRequest request) {

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException(ApiMessages.PASSWORD_MISMATCHED);
        }
    }

    private User createNewUser(RegisterRequest request) {

        return authenticationMapper.toUser(request);
    }

    private User updateExistingUser(User existingUser, RegisterRequest request) {

        existingUser.setFullName(request.getFullName());
        existingUser.setPassword(request.getPassword());
        existingUser.setCollege(request.getCollege());
        existingUser.setAcademicYear(request.getAcademicYear());
        existingUser.setGender(request.getGender());

        return existingUser;
    }

    private RegisterResponse buildRegisterResponse(User user) {

        return authenticationMapper.toRegisterResponse(user);
    }
}