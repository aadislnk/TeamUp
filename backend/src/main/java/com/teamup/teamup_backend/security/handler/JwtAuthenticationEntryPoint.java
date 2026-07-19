package com.teamup.teamup_backend.security.handler;

import com.teamup.teamup_backend.constant.ApiMessages;
import com.teamup.teamup_backend.dto.common.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.security.core.AuthenticationException;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;


    public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        ErrorResponse error = ErrorResponse.builder()
                .success(false)
                .status(HttpServletResponse.SC_UNAUTHORIZED)
                .path(request.getRequestURI())
                .message(ApiMessages.UNAUTHORIZED)
                .timestamp(LocalDateTime.now())
                .build();
        objectMapper.writeValue(response.getOutputStream(), error);

    }
}
