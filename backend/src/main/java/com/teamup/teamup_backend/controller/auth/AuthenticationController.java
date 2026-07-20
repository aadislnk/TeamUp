package com.teamup.teamup_backend.controller.auth;

import com.teamup.teamup_backend.constant.ApiMessages;
import com.teamup.teamup_backend.constant.ApiPaths;
import com.teamup.teamup_backend.dto.request.LoginRequest;
import com.teamup.teamup_backend.dto.request.RegisterRequest;
import com.teamup.teamup_backend.dto.common.ApiResponse;
import com.teamup.teamup_backend.dto.response.LoginResponse;
import com.teamup.teamup_backend.dto.response.RegisterResponse;
import com.teamup.teamup_backend.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPaths.AUTH)
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(
            @Valid @RequestBody RegisterRequest request) {

        RegisterResponse response =
                authenticationService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        ApiMessages.REGISTRATION_SUCCESS,
                        response
                ));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response =
                authenticationService.login(request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        ApiMessages.LOGIN_SUCCESS,
                        response
                )
        );
    }
}