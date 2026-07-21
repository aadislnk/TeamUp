package com.teamup.teamup_backend.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Year;

@Component
@RequiredArgsConstructor
public class EmailTemplateBuilder {

    private final EmailTemplateLoader templateLoader;

    @Value("${spring.application.name}")
    private String appName;

    @Value("${app.frontend-url}")
    private String appUrl;

    public String buildVerificationEmail(
            String userName,
            String otp,
            int expiryMinutes
    ) {
        return templateLoader.loadTemplate("verification-email.html")
                .replace("{{APP_NAME}}", appName)
                .replace("{{USER_NAME}}", userName)
                .replace("{{OTP}}", otp)
                .replace("{{EXPIRY_MINUTES}}", String.valueOf(expiryMinutes))
                .replace("{{APP_URL}}", appUrl)
                .replace("{{CURRENT_YEAR}}", currentYear());
    }

    public String buildWelcomeEmail(
            String userName
    ) {
        return templateLoader.loadTemplate("welcome-email.html")
                .replace("{{APP_NAME}}", appName)
                .replace("{{USER_NAME}}", userName)
                .replace("{{APP_URL}}", appUrl)
                .replace("{{CURRENT_YEAR}}", currentYear());
    }

    public String buildJoinRequestReceivedEmail(
            String leaderName,
            String applicantName,
            String teamName,
            String applicantRole
    ) {
        return templateLoader.loadTemplate("join-request-received.html")
                .replace("{{APP_NAME}}", appName)
                .replace("{{LEADER_NAME}}", leaderName)
                .replace("{{APPLICANT_NAME}}", applicantName)
                .replace("{{TEAM_NAME}}", teamName)
                .replace("{{APPLICANT_ROLE}}", applicantRole)
                .replace("{{APP_URL}}", appUrl)
                .replace("{{CURRENT_YEAR}}", currentYear());
    }

    public String buildJoinRequestAcceptedEmail(
            String userName,
            String teamName,
            String teamLeader
    ) {
        return templateLoader.loadTemplate("join-request-accepted.html")
                .replace("{{APP_NAME}}", appName)
                .replace("{{USER_NAME}}", userName)
                .replace("{{TEAM_NAME}}", teamName)
                .replace("{{TEAM_LEADER}}", teamLeader)
                .replace("{{APP_URL}}", appUrl)
                .replace("{{CURRENT_YEAR}}", currentYear());
    }

    public String buildJoinRequestRejectedEmail(
            String userName,
            String teamName,
            String teamLeader
    ) {
        return templateLoader.loadTemplate("join-request-rejected.html")
                .replace("{{APP_NAME}}", appName)
                .replace("{{USER_NAME}}", userName)
                .replace("{{TEAM_NAME}}", teamName)
                .replace("{{TEAM_LEADER}}", teamLeader)
                .replace("{{APP_URL}}", appUrl)
                .replace("{{CURRENT_YEAR}}", currentYear());
    }

    public String buildTeamInvitationEmail(
            String userName,
            String teamName,
            String teamLeader,
            String eventName,
            String teamRole
    ) {
        return templateLoader.loadTemplate("team-invitation.html")
                .replace("{{APP_NAME}}", appName)
                .replace("{{USER_NAME}}", userName)
                .replace("{{TEAM_NAME}}", teamName)
                .replace("{{TEAM_LEADER}}", teamLeader)
                .replace("{{EVENT_NAME}}", eventName)
                .replace("{{TEAM_ROLE}}", teamRole)
                .replace("{{APP_URL}}", appUrl)
                .replace("{{CURRENT_YEAR}}", currentYear());
    }

    private String currentYear() {
        return String.valueOf(Year.now().getValue());
    }
}