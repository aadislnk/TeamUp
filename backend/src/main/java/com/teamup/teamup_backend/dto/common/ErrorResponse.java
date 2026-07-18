package com.teamup.teamup_backend.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private boolean success;

    private int status;

    private String message;

    private List<ValidationError> errors;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    private String path;
}
