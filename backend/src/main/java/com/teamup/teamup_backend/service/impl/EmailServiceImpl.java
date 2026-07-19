package com.teamup.teamup_backend.service.impl;

import com.teamup.teamup_backend.config.MailProperties;
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

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final Logger log =
            LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;
    private final EmailTemplateBuilder templateBuilder;

    @Override
    public void sendVerificationEmail(
            String to,
            String userName,
            String otp,
            int expiryMinutes
    ) {

        String html = templateBuilder.buildVerificationEmail(
                userName,
                otp,
                expiryMinutes
        );

        try {

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(
                    mailProperties.getFromAddress(),
                    mailProperties.getFromName()
            );

            helper.setTo(to);

            helper.setSubject(EmailSubjects.VERIFY_EMAIL);

            helper.setText(html, true);

            mailSender.send(message);

            log.info("Verification email sent successfully to {}", to);

        } catch (MailAuthenticationException ex) {

            log.error("Mail authentication failed.", ex);

            throw ex;

        } catch (MessagingException ex) {

            log.error("Failed to build email for {}", to, ex);

            throw new RuntimeException("Unable to prepare email.", ex);

        } catch (MailException ex) {

            log.error("Failed to send verification email to {}", to, ex);

            throw ex;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}