package com.teamup.teamup_backend.constant;

public final class ApiMessages {
    private ApiMessages() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated.");
        //we do this taaki iska object na bna sake becus it makes no sence!!
    }
    public static final String USER_REGISTERED_SUCCESS = "User registered successfully!";
    public static final String LOGIN_SUCCESS = "Login successful.";
    public static final String LOGOUT_SUCCESS = "Logout successful.";
    public static final String PASSWORD_CHANGED_SUCCESS = "Password changed successfully.";


    public static final String INTERNAL_SERVER_ERROR = "An unexpected error occurred.";
    public static final String RESOURCE_NOT_FOUND = "Requested resource not found.";
    public static final String ACCESS_DENIED = "Access denied.";
    public static final String UNAUTHORIZED = "Authentication required.";
}
