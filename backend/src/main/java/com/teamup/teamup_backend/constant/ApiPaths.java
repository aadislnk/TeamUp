package com.teamup.teamup_backend.constant;

public final class ApiPaths {

    private ApiPaths() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated.");
    }

    // Base API Path
    public static final String API_BASE = "/api/v1";

    // Authentication
    public static final String AUTH = API_BASE + "/auth";

    // Users
    public static final String USERS = API_BASE + "/users";

    public static final String CURRENT_USER = "/me";

    public static final String USER_ID = "/{userId}";

    // Teams
    public static final String TEAMS = API_BASE + "/teams";

    // Events
    public static final String EVENTS = API_BASE + "/events";

    // Skills
    public static final String SKILLS = API_BASE + "/skills";

    public static final String USER_SKILLS = "/skills";

    public static final String SKILL_ID = "/{skillId}";

    // Team Invitations
    public static final String INVITATIONS = API_BASE + "/invitations";

    // Notifications
    public static final String NOTIFICATIONS = API_BASE + "/notifications";
}