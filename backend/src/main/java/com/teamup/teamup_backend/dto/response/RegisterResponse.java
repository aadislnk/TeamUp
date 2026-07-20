package com.teamup.teamup_backend.dto.response;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterResponse {

    private String email;

    private String message;

    private boolean verificationRequired;
}
