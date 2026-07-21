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

    public static final String SKILLS_FETCHED =  "Skills fetched successfully.";
    public static final String SKILLS_ADDED =  "Skills added successfully.";
    public static final String SKILL_REMOVED =  "Skills removed successfully.";
    public static final String USERS_FETCHED = "Users fetched successfully.";

    public static final String TEAM_CREATED = "Team created successfully.";
    public static final String TEAM_UPDATED = "Team updated successfully.";
    public static final String TEAM_DELETED = "Team deleted successfully.";
    public static final String TEAM_FETCHED = "Team fetched successfully.";
    public static final String TEAMS_FETCHED = "Teams fetched successfully.";
    public static final String RECRUITMENT_UPDATED = "Recruitment status updated successfully.";
    public static final String WHATSAPP_LINK_UPDATED = "WhatsApp group link updated successfully.";

    public static final String ONLY_TEAM_LEADER_ALLOWED = "Only team leader is allowed to perform this action.";
    public static final String MAX_MEMBERS_LESS_THAN_CURRENT = "Maximum members cannot be less than the current number of team members.";
    public static final String TEAM_ALREADY_EXISTS =  "Team already exists.";
    public static final String TEAM_NOT_FOUND = "Team not found.";
    public static final String EVENT_NOT_FOUND = "Event not found.";
    public static final String ALREADY_LEADING_TEAM_FOR_EVENT = "You are already leading a team for this event.";
    public static final String CANNOT_DELETE_TEAM_WITH_MEMBERS = "Cannot delete a team that has members. Please remove all members before deleting the team.";
    public static final String TEAM_NOT_OPEN = "Team is not open for recruitment. You cannot join this team.";
    public static final String RECRUITMENT_CLOSED = "Recruitment for this team is closed. You cannot join this team.";
    public static final String TEAM_FULL = "This team has reached its maximum member capacity. You cannot join this team.";

    public static final String REQUIRED_SKILL_ADDED = "Required skill added successfully.";

    public static final String REQUIRED_SKILL_REMOVED = "Required skill removed successfully.";

    public static final String REQUIRED_SKILL_ALREADY_EXISTS = "This skill has already been added to the team.";

    public static final String REQUIRED_SKILL_NOT_FOUND = "Required skill not found for this team.";

    public static final String INVALID_SKILL = "Selected skill does not exist.";

}
