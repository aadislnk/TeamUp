package com.teamup.teamup_backend.constant;

public final class ValidationMessages {

    private ValidationMessages() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated.");
    }

    // Common Validation Messages
    public static final String REQUIRED_FIELD = "This field is required.";
    public static final String INVALID_VALUE = "Invalid value.";

    // User Validation Messages
    public static final String FIRST_NAME_REQUIRED = "First name is required.";
    public static final String LAST_NAME_REQUIRED = "Last name is required.";
    public static final String EMAIL_REQUIRED = "Email is required.";
    public static final String INVALID_EMAIL = "Please enter a valid email address.";

    // Password
    public static final String PASSWORD_REQUIRED = "Password is required.";
    public static final String PASSWORD_TOO_SHORT = "Password must be at least 8 characters long.";
    public static final String PASSWORD_TOO_LONG = "Password must not exceed 100 characters.";

    // Team
    public static final String TEAM_NAME_REQUIRED = "Team name is required.";
    public static final String TEAM_NAME_TOO_LONG = "Team name must not exceed 100 characters.";

    // Event
    public static final String EVENT_TITLE_REQUIRED = "Event title is required.";
    public static final String EVENT_DESCRIPTION_TOO_LONG = "Event description must not exceed 1000 characters.";
}