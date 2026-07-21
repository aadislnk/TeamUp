package com.teamup.teamup_backend.dto.request;

import com.teamup.teamup_backend.constant.ApplicationConstants;
import com.teamup.teamup_backend.constant.ValidationMessages;
import com.teamup.teamup_backend.enums.Avatar;
import com.teamup.teamup_backend.enums.PreferredRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProfileRequest {

    @NotBlank
    @Size(min = 2, max = 100)
    private String fullName;

    private String bio;

    private PreferredRole preferredRole;

    @URL
    private String githubUrl;

    @URL
    private String linkedinUrl;

    @Pattern(
            regexp = ApplicationConstants.WHATSAPP_NUMBER_REGEX,
            message = ValidationMessages.INVALID_WHATSAPP_NUMBER
    )
    private String whatsappNumber;

    private Avatar avatar;

}