package com.teamup.teamup_backend.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddSkillsRequest {

    @NotEmpty
    private List<@NotNull Long> skillIds;

}
