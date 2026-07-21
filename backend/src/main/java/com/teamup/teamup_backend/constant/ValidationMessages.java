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

    public static final String INVALID_AVATAR = "Please select a valid avatar.";

    public static final String INVALID_WHATSAPP_NUMBER = "Please enter a valid WhatsApp number.";


    public static final String TEAM_NAME_BLANK = "Team name cannot be blank.";
    public static final String TEAM_NAME_MIN = "Team name must contain at least 3 characters.";
    public static final String TEAM_NAME_MAX = "Team name cannot exceed 100 characters.";
    public static final String TEAM_DESCRIPTION_MAX = "Description cannot exceed 2000 characters.";
    public static final String MAX_MEMBERS_REQUIRED = "Maximum team members is required.";
    public static final String MAX_MEMBERS_MIN = "Maximum team members must be at least 2.";
    public static final String MAX_MEMBERS_MAX = "Maximum team members cannot exceed 50.";
    public static final String RECRUITMENT_STATUS_REQUIRED = "Recruitment status is required.";
    public static final String WHATSAPP_LINK_INVALID = "Please provide a valid WhatsApp group link.";
    public static final String TEAM_STATUS_REQUIRED = "Team status is required.";
}