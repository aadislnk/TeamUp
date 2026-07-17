package com.teamup.teamup_backend.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationError {

    private String field; // name of field that caused error
    private String message;
}
//field nullable hai taki validation error(password is 8chars,email required etc) k sath
//agar buisness error(like ream is full) ho to wo bhi cover ho jaye