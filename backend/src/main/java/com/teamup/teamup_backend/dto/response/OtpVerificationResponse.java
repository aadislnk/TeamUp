package com.teamup.teamup_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OtpVerificationResponse {

    private String message;

    private boolean verified;

}