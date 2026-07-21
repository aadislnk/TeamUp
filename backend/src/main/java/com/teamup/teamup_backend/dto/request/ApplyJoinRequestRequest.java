package com.teamup.teamup_backend.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyJoinRequestRequest {

    @Size(max = 500)
    private String message;
}