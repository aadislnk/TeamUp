package com.teamup.teamup_backend.constant;

public final class ApplicationConstants {

    private ApplicationConstants() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated.");
    }

    // Pagination Defaults
    public static final int DEFAULT_PAGE_NUMBER = 0;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 100;

    // Common String Constants
   // public static final String EMPTY_STRING = "";
    public static final String SYSTEM = "SYSTEM";
    public static final String UNKNOWN = "UNKNOWN";

    public static final String WHATSAPP_NUMBER_REGEX = "^\\+?[1-9]\\d{7,14}$";

    public static final int TEAM_NAME_MIN_LENGTH = 3;
    public static final int TEAM_NAME_MAX_LENGTH = 100;
    public static final int TEAM_DESCRIPTION_MAX_LENGTH = 2000;
    public static final int TEAM_MIN_MEMBERS = 2;
    public static final int TEAM_MAX_MEMBERS = 50;
    public static final int TEAM_WHATSAPP_LINK_MAX_LENGTH = 255;
}