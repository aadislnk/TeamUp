package com.teamup.teamup_backend.service;

public interface EmailService {

    void sendVerificationEmail(
            String recipientEmail,
            String userName,
            String otp,
            int expiryMinutes
    );

    void sendWelcomeEmail(
            String recipientEmail,
            String userName
    );

    void sendJoinRequestReceivedEmail(
            String recipientEmail,
            String leaderName,
            String applicantName,
            String teamName,
            String applicantRole
    );

    void sendJoinRequestAcceptedEmail(
            String recipientEmail,
            String userName,
            String teamName,
            String teamLeader
    );

    void sendJoinRequestRejectedEmail(
            String recipientEmail,
            String userName,
            String teamName,
            String teamLeader
    );

    void sendTeamInvitationEmail(
            String recipientEmail,
            String userName,
            String teamName,
            String teamLeader,
            String eventName,
            String teamRole
    );

}