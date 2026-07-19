package com.teamup.teamup_backend.controller.auth;

import com.teamup.teamup_backend.constant.ApiMessages;
import com.teamup.teamup_backend.constant.ApiPaths;
import com.teamup.teamup_backend.dto.request.ResendOtpRequest;
import com.teamup.teamup_backend.dto.request.SendOtpRequest;
import com.teamup.teamup_backend.dto.request.VerifyOtpRequest;
import com.teamup.teamup_backend.dto.common.ApiResponse;
import com.teamup.teamup_backend.dto.response.OtpSentResponse;
import com.teamup.teamup_backend.dto.response.OtpVerificationResponse;
import com.teamup.teamup_backend.dto.response.ResendOtpResponse;
import com.teamup.teamup_backend.service.EmailVerificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPaths.AUTH)
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<OtpSentResponse>> sendOtp(
            @Valid @RequestBody SendOtpRequest request) {

        emailVerificationService.sendVerificationOtp(request.getEmail());

        OtpSentResponse response = OtpSentResponse.builder()
                .message(ApiMessages.OTP_SENT_SUCCESS)
                .build();

        return ResponseEntity.ok( ApiResponse.success(ApiMessages.OTP_SENT_SUCCESS, response));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<OtpVerificationResponse>> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request) {

        emailVerificationService.verifyEmail(
                request.getEmail(),
                request.getOtp()
        );

        OtpVerificationResponse response = OtpVerificationResponse.builder()
                .message(ApiMessages.OTP_VERIFIED_SUCCESS)
                .verified(true)
                .build();

        return ResponseEntity.ok(ApiResponse.success(ApiMessages.OTP_VERIFIED_SUCCESS, response));
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<ApiResponse<ResendOtpResponse>> resendOtp(
            @Valid @RequestBody ResendOtpRequest request) {

        emailVerificationService.resendVerificationOtp(request.getEmail());

        ResendOtpResponse response = ResendOtpResponse.builder()
                .message(ApiMessages.OTP_RESENT_SUCCESS)
                .build();

        return ResponseEntity.ok(ApiResponse.success(ApiMessages.OTP_RESENT_SUCCESS, response));
    }
}
