package com.teamup.teamup_backend.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T>{

    private boolean success; //kya request succesfull thi!

    private String message; //human readable response msg.

    private T data; //actual response payload

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now(); //time when resp was generated
}
