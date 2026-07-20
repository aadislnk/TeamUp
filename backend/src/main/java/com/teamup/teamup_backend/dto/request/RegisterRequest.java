package com.teamup.teamup_backend.dto.request;

import com.teamup.teamup_backend.enums.AcademicYear;
import com.teamup.teamup_backend.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    @NotBlank
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String fullName;

    @NotBlank
    @Email
    @Size(max=255)
    private String email;

    @NotBlank
    @Size(min = 8, max = 100)
    private String password;

    @NotBlank
    @Size(min = 8, max = 100)
    private String confirmPassword;

    @NotBlank
    @Size(max = 150)
    private String college;

    @NotNull
    AcademicYear academicYear;
    @NotNull
    Gender gender;
}
