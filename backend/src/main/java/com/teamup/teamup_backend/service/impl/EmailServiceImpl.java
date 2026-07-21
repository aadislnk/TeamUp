package com.teamup.teamup_backend.service.impl;

import com.teamup.teamup_backend.config.MailProperties;
import com.teamup.teamup_backend.exception.EmailSendingException;
import com.teamup.teamup_backend.mail.EmailSubjects;
import com.teamup.teamup_backend.mail.EmailTemplateBuilder;
import com.teamup.teamup_backend.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final Logger log =
            LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;
    private final EmailTemplateBuilder emailTemplateBuilder;

    @Override
    public void sendVerificationEmail(
            String recipientEmail,
            String userName,
            String otp,
            int expiryMinutes
    ) {
        String html = emailTemplateBuilder.buildVerificationEmail(
                userName,
                otp,
                expiryMinutes
        );

        sendEmail(
                recipientEmail,
                EmailSubjects.VERIFY_EMAIL,
                html
        );
    }

    @Override
    public void sendWelcomeEmail(
            String recipientEmail,
            String userName
    ) {
        String html = emailTemplateBuilder.buildWelcomeEmail(
                userName
        );

        sendEmail(
                recipientEmail,
                EmailSubjects.WELCOME_EMAIL,
                html
        );
    }

    @Override
    public void sendJoinRequestReceivedEmail(
            String recipientEmail,
            String leaderName,
            String applicantName,
            String teamName,
            String applicantRole
    ) {
        String html = emailTemplateBuilder.buildJoinRequestReceivedEmail(
                leaderName,
                applicantName,
                teamName,
                applicantRole
        );

        sendEmail(
                recipientEmail,
                EmailSubjects.JOIN_REQUEST_RECEIVED,
                html
        );
    }

    @Override
    public void sendJoinRequestAcceptedEmail(
            String recipientEmail,
            String userName,
            String teamName,
            String teamLeader
    ) {
        String html = emailTemplateBuilder.buildJoinRequestAcceptedEmail(
                userName,
                teamName,
                teamLeader
        );

        sendEmail(
                recipientEmail,
                EmailSubjects.JOIN_REQUEST_ACCEPTED,
                html
        );
    }

    @Override
    public void sendJoinRequestRejectedEmail(
            String recipientEmail,
            String userName,
            String teamName,
            String teamLeader
    ) {
        String html = emailTemplateBuilder.buildJoinRequestRejectedEmail(
                userName,
                teamName,
                teamLeader
        );

        sendEmail(
                recipientEmail,
                EmailSubjects.JOIN_REQUEST_REJECTED,
                html
        );
    }

    @Override
    public void sendTeamInvitationEmail(
            String recipientEmail,
            String userName,
            String teamName,
            String teamLeader,
            String eventName,
            String teamRole
    ) {
        String html = emailTemplateBuilder.buildTeamInvitationEmail(
                userName,
                teamName,
                teamLeader,
                eventName,
                teamRole
        );

        sendEmail(
                recipientEmail,
                EmailSubjects.TEAM_INVITATION,
                html
        );
    }




    private void sendEmail(
            String recipientEmail,
            String subject,
            String html
    ) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            helper.setFrom(
                    mailProperties.getFromAddress(),
                    mailProperties.getFromName()
            );
            helper.setTo(recipientEmail);
            helper.setSubject(subject);
            helper.setText(html, true);

            mailSender.send(message);

            log.info(
                    "Email sent successfully. Recipient: {}, Subject: {}",
                    recipientEmail,
                    subject
            );

        } catch (MessagingException exception) {

            log.error(
                    "Failed to prepare email. Recipient: {}, Subject: {}",
                    recipientEmail,
                    subject,
                    exception
            );

            throw new EmailSendingException(
                    "Failed to prepare email."
            );

        } catch (MailException exception) {

            log.error(
                    "Failed to send email. Recipient: {}, Subject: {}",
                    recipientEmail,
                    subject,
                    exception
            );

            throw new EmailSendingException(
                    "Failed to send email."
            );
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}