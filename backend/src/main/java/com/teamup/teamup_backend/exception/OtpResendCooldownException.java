package com.teamup.teamup_backend.exception;

public class OtpResendCooldownException extends RuntimeException {
    public OtpResendCooldownException(String message) {
        super(message);
    }
}
