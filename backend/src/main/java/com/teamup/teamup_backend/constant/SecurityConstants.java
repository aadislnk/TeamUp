package com.teamup.teamup_backend.constant;

public final class SecurityConstants {

    private SecurityConstants() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated.");
    }

    // HTTP Headers
    public static final String AUTHORIZATION_HEADER = "Authorization";

    // JWT
    public static final String BEARER_PREFIX = "Bearer ";

    // Content Type
    public static final String APPLICATION_JSON = "application/json";

    public static final String ROLE_CLAIM = "role";
    public static final String EMAIL_CLAIM = "email";

    public static final String[] PUBLIC_URLS = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/error",
            "/api/auth/**"
    };

}