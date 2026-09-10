import type { AcademicYear, Gender, MyProfileResponse } from './user';

/**
 * Authentication state machine status.
 */
export type AuthStatus = 'loading' | 'authenticated' | 'unauthenticated';

/**
 * Interface for the centralized authentication context.
 */
export interface AuthContextValue {
  /** Authenticated user profile, or null when unauthenticated / loading */
  user: MyProfileResponse | null;
  /** Discriminated authentication lifecycle state */
  status: AuthStatus;
  /** True when authentication is resolving initial session */
  isLoading: boolean;
  /** True when a valid user session is active */
  isAuthenticated: boolean;
  /** True when no active session exists */
  isUnauthenticated: boolean;
  /** Set session token and load profile */
  login: (accessToken: string) => Promise<void>;
  /** Clear session token and invalidate auth state */
  logout: () => void;
}


/**
 * System authorization role.
 */
export type Role = 'USER';

/**
 * OTP purpose type.
 */
export type OtpPurpose = 'EMAIL_VERIFICATION';

/**
 * OTP token status.
 */
export type TokenStatus = 'ACTIVE' | 'VERIFIED' | 'EXPIRED' | 'REVOKED';

/**
 * Request payload for user login.
 */
export interface LoginRequest {
  email: string;
  password: string;
}

/**
 * Request payload for user registration.
 */
export interface RegisterRequest {
  fullName: string;
  email: string;
  password: string;
  confirmPassword: string;
  college: string;
  academicYear: AcademicYear;
  gender: Gender;
}

/**
 * Request payload to send an email verification OTP.
 */
export interface SendOtpRequest {
  email: string;
}

/**
 * Request payload to verify an email OTP code.
 */
export interface VerifyOtpRequest {
  email: string;
  otp: string;
}

/**
 * Request payload to resend an email verification OTP.
 */
export interface ResendOtpRequest {
  email: string;
}

/**
 * Authentication response containing JWT and user identity.
 */
export interface LoginResponse {
  accessToken: string;
  tokenType: string;
  expiresIn: number;
  email: string;
  fullName: string;
  role: Role;
}

/**
 * Registration response payload.
 */
export interface RegisterResponse {
  email: string;
  message: string;
  verificationRequired: boolean;
}

/**
 * Response payload returned after an OTP is dispatched.
 */
export interface OtpSentResponse {
  message: string;
}

/**
 * Response payload returned after verifying an OTP.
 */
export interface OtpVerificationResponse {
  message: string;
  verified: boolean;
}

/**
 * Response payload returned after resending an OTP.
 */
export interface ResendOtpResponse {
  message: string;
}
