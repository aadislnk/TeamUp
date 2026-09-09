import api from '../lib/api';
import type {
  ApiResponse,
  LoginRequest,
  LoginResponse,
  OtpSentResponse,
  OtpVerificationResponse,
  RegisterRequest,
  RegisterResponse,
  ResendOtpRequest,
  ResendOtpResponse,
  SendOtpRequest,
  VerifyOtpRequest,
} from '../types';

/**
 * Service for authentication and email OTP verification workflows.
 */
export const authService = {
  /**
   * Register a new user account.
   */
  async register(payload: RegisterRequest): Promise<RegisterResponse> {
    const response = await api.post<ApiResponse<RegisterResponse>>('/auth/register', payload);
    return response.data.data;
  },

  /**
   * Authenticate user with credentials and obtain JWT access token.
   */
  async login(payload: LoginRequest): Promise<LoginResponse> {
    const response = await api.post<ApiResponse<LoginResponse>>('/auth/login', payload);
    return response.data.data;
  },

  /**
   * Request an email verification OTP for the provided email address.
   */
  async sendOtp(payload: SendOtpRequest): Promise<OtpSentResponse> {
    const response = await api.post<ApiResponse<OtpSentResponse>>('/auth/send-otp', payload);
    return response.data.data;
  },

  /**
   * Verify an email verification OTP code.
   */
  async verifyOtp(payload: VerifyOtpRequest): Promise<OtpVerificationResponse> {
    const response = await api.post<ApiResponse<OtpVerificationResponse>>(
      '/auth/verify-otp',
      payload
    );
    return response.data.data;
  },

  /**
   * Resend an email verification OTP code.
   */
  async resendOtp(payload: ResendOtpRequest): Promise<ResendOtpResponse> {
    const response = await api.post<ApiResponse<ResendOtpResponse>>('/auth/resend-otp', payload);
    return response.data.data;
  },
};
