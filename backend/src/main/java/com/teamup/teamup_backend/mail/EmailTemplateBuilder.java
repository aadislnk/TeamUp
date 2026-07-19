package com.teamup.teamup_backend.mail;

import org.springframework.stereotype.Component;

@Component
public class EmailTemplateBuilder {

    public String buildVerificationEmail(
            String userName,
            String otp,
            int expiryMinutes
    ) {

        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <title>Verify Your Email</title>
                </head>
                <body style="margin:0;padding:0;background-color:#f5f5f5;font-family:Arial,Helvetica,sans-serif;">

                    <table width="100%%" cellpadding="0" cellspacing="0" style="padding:40px 0;">
                        <tr>
                            <td align="center">

                                <table width="600" cellpadding="0" cellspacing="0"
                                       style="background:#ffffff;border-radius:10px;
                                              padding:40px;
                                              box-shadow:0 2px 10px rgba(0,0,0,0.08);">

                                    <tr>
                                        <td align="center">
                                            <h1 style="margin:0;color:#2563eb;">
                                                TeamUp
                                            </h1>

                                            <p style="color:#666;margin-top:8px;">
                                                Email Verification
                                            </p>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td style="padding-top:30px;">

                                            <p style="font-size:16px;color:#333;">
                                                Hello <strong>%s</strong>,
                                            </p>

                                            <p style="font-size:15px;color:#555;line-height:1.6;">
                                                Thank you for registering with TeamUp.
                                                Use the verification code below to verify your email address.
                                            </p>

                                        </td>
                                    </tr>

                                    <tr>
                                        <td align="center" style="padding:30px 0;">

                                            <div style="
                                                display:inline-block;
                                                padding:18px 36px;
                                                font-size:32px;
                                                font-weight:bold;
                                                letter-spacing:8px;
                                                background:#2563eb;
                                                color:white;
                                                border-radius:8px;">

                                                %s

                                            </div>

                                        </td>
                                    </tr>

                                    <tr>
                                        <td>

                                            <p style="font-size:15px;color:#555;">
                                                This verification code will expire in
                                                <strong>%d minutes</strong>.
                                            </p>

                                            <p style="font-size:15px;color:#555;">
                                                If you didn't request this verification,
                                                you can safely ignore this email.
                                            </p>

                                        </td>
                                    </tr>

                                    <tr>
                                        <td style="padding-top:30px;border-top:1px solid #eeeeee;">

                                            <p style="font-size:13px;color:#999;text-align:center;">
                                                This is an automated email from TeamUp.
                                                Please do not reply.
                                            </p>

                                        </td>
                                    </tr>

                                </table>

                            </td>
                        </tr>
                    </table>

                </body>
                </html>
                """.formatted(
                userName,
                otp,
                expiryMinutes
        );
    }

}
