package com.teamup.teamup_backend.service.impl;

import com.teamup.teamup_backend.dto.request.RegisterRequest;
import com.teamup.teamup_backend.dto.response.RegisterResponse;
import com.teamup.teamup_backend.entity.User;
import com.teamup.teamup_backend.exception.BadRequestException;
import com.teamup.teamup_backend.exception.ConflictException;
import com.teamup.teamup_backend.mapper.AuthenticationMapper;
import com.teamup.teamup_backend.repository.UserRepository;
import com.teamup.teamup_backend.service.AuthenticationService;
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

    @Override
    public RegisterResponse register(RegisterRequest request) {

        validatePasswords(request);

        User user = userRepository.findByEmail(request.getEmail())
                .map(existingUser -> {

                    if (Boolean.TRUE.equals(existingUser.getEmailVerified())) {
                        throw new ConflictException("Email is already registered.");
                    }

                    return updateExistingUser(existingUser, request);
                })
                .orElseGet(() -> createNewUser(request));

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        return buildRegisterResponse(savedUser);
    }

    private void validatePasswords(RegisterRequest request) {

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match.");
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