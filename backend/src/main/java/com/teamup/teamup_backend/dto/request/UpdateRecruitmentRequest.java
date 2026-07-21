package com.teamup.teamup_backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRecruitmentRequest {

    @NotNull
    private Boolean recruitmentOpen;
}