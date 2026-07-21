package com.teamup.teamup_backend.constant;

public final class ApiMessages {



    private ApiMessages() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated.");
        //we do this taaki iska object na bna sake becus it makes no sence!!
    }
    public static final String REGISTRATION_SUCCESS = "User registered successfully!";
    public static final String LOGIN_SUCCESS = "Login successful.";
    public static final String LOGOUT_SUCCESS = "Logout successful.";
    public static final String PASSWORD_CHANGED_SUCCESS = "Password changed successfully.";


    public static final String INTERNAL_SERVER_ERROR = "An unexpected error occurred.";
    public static final String RESOURCE_NOT_FOUND = "Requested resource not found.";
    public static final String ACCESS_DENIED = "Access denied.";
    public static final String UNAUTHORIZED = "Authentication required.";

    public static final String OTP_SENT_SUCCESS = "OTP sent successfully.";
    public static final String OTP_VERIFIED_SUCCESS = "Email verified successfully.";
    public static final String OTP_RESENT_SUCCESS = "OTP resent successfully.";

    public static final String EMAIL_ALREADY_REGISTERED = "Email is already registered.";
    public static final String PASSWORD_MISMATCHED = "Passwords do not match.";
    public static final String INVALID_CREDENTIALS = "Invalid credentials.";
    public static final String EMAIL_NOT_VERIFIED = "Email is not verified. Please verify your email before proceeding.";

    public static final String PROFILE_FETCHED = "Profile fetched successfully.";

    public static final String PROFILE_UPDATED = "Profile updated successfully.";

    public static final String PROFILE_AVATAR_UPDATED = "Profile avatar updated successfully.";
    public static final String PROFILE_AVATAR_REMOVED = "Profile avatar removed successfully.";
}
