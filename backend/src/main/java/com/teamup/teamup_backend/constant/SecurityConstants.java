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

}