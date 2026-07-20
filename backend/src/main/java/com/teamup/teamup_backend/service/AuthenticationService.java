package com.teamup.teamup_backend.service;

import com.teamup.teamup_backend.dto.request.RegisterRequest;
import com.teamup.teamup_backend.dto.response.RegisterResponse;
import com.teamup.teamup_backend.exception.BadRequestException;
import com.teamup.teamup_backend.exception.ConflictException;

public interface AuthenticationService {
    RegisterResponse register(RegisterRequest request) throws ConflictException, BadRequestException;
}
