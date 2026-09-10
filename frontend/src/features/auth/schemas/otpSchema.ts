import { z } from 'zod';

/**
 * Validation schema for email OTP verification.
 * Enforces 6-digit numeric string matching backend VerifyOtpRequest.
 */
export const otpSchema = z.object({
  email: z
    .string()
    .trim()
    .min(1, 'Email address is required')
    .email('Please enter a valid email address'),
  otp: z
    .string()
    .trim()
    .min(1, 'OTP is required')
    .regex(/^\d{6}$/, 'OTP must be 6 digits'),
});

export type OtpFormData = z.infer<typeof otpSchema>;
