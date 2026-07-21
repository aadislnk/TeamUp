package com.teamup.teamup_backend.dto.request;

import com.teamup.teamup_backend.enums.Avatar;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProfilePictureRequest {

    @NotNull(message = "Avatar is required.")
    private Avatar avatar;

}