package com.teamup.teamup_backend.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateWhatsAppRequest {

    @Pattern(
            regexp = "^(https://chat\\.whatsapp\\.com/.*)?$",
            message = "Invalid WhatsApp group link."
    )
    private String whatsappGroupLink;
}