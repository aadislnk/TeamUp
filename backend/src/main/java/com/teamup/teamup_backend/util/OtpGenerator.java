package com.teamup.teamup_backend.util;

import com.teamup.teamup_backend.config.OtpProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;


@Component
@RequiredArgsConstructor
public class OtpGenerator {
    private final SecureRandom secureRandom = new SecureRandom();

    private final OtpProperties otpProperties;

    public String generateOtp(){
       StringBuilder otp = new StringBuilder();
       for(int i = 0; i < otpProperties.getLength(); i++){
           otp.append(secureRandom.nextInt(10));
       }
       return otp.toString();
    }
}
