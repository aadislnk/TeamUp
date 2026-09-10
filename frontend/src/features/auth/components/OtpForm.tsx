import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { Input, Button } from '../../../components/ui';
import { authService } from '../../../services';
import { zodResolver } from '../../../lib/form';
import { useAuth } from '../../../hooks';
import { otpSchema, type OtpFormData } from '../schemas/otpSchema';
import type { ErrorResponse, OtpVerificationResponse } from '../../../types';

export interface OtpFormProps {
  email: string;
  onSuccess?: (response: OtpVerificationResponse) => void;
  className?: string;
}

/**
 * OTP verification form component for email verification flow.
 *
 * Validates 6-digit OTP code, calls authService.verifyOtp(), handles errors,
 * supports resending OTP code, and handles authentication handoff if token is present.
 */
export const OtpForm: React.FC<OtpFormProps> = ({ email, onSuccess, className }) => {
  const navigate = useNavigate();
  const { login } = useAuth();
  const [formError, setFormError] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);
  const [isResending, setIsResending] = useState(false);
  const [resendStatus, setResendStatus] = useState<{ message: string; isError: boolean } | null>(
    null
  );

  const {
    register,
    handleSubmit,
    setError,
    formState: { errors, isSubmitting },
  } = useForm<OtpFormData>({
    resolver: zodResolver(otpSchema),
    mode: 'onTouched',
    defaultValues: {
      email,
      otp: '',
    },
  });

  const onSubmit = async (data: OtpFormData) => {
    setFormError(null);
    setSuccessMessage(null);
    setResendStatus(null);

    try {
      const response = await authService.verifyOtp({
        email: data.email,
        otp: data.otp,
      });

      // Check if response contains an access token (per strict authentication rule)
      const tokenResponse = response as unknown as { accessToken?: string };
      if (tokenResponse.accessToken) {
        await login(tokenResponse.accessToken);
      }

      if (onSuccess) {
        onSuccess(response);
      } else {
        setSuccessMessage(
          response.message || 'Email verified successfully! Redirecting to login...'
        );
        setTimeout(() => {
          navigate('/login', {
            state: { emailVerified: true, email: data.email },
          });
        }, 1500);
      }
    } catch (err: unknown) {
      if (axios.isAxiosError<ErrorResponse>(err)) {
        const errorData = err.response?.data;

        if (errorData?.errors && errorData.errors.length > 0) {
          let mappedField = false;
          for (const fieldErr of errorData.errors) {
            if (fieldErr.field === 'otp' && fieldErr.message) {
              setError('otp', {
                type: 'server',
                message: fieldErr.message,
              });
              mappedField = true;
            }
          }
          if (!mappedField && errorData.message) {
            setFormError(errorData.message);
          }
        } else if (errorData?.message) {
          setFormError(errorData.message);
        } else if (err.response?.status === 400) {
          setFormError('Invalid or expired OTP code. Please check and try again.');
        } else {
          setFormError('Verification failed. Please check your code and try again.');
        }
      } else {
        setFormError('Unable to connect to the server. Please check your network connection.');
      }
    }
  };

  const handleResendOtp = async () => {
    if (isResending || isSubmitting) return;

    setIsResending(true);
    setResendStatus(null);
    setFormError(null);

    try {
      const response = await authService.resendOtp({ email });
      setResendStatus({
        message: response.message || 'Verification code resent successfully.',
        isError: false,
      });
    } catch (err: unknown) {
      if (axios.isAxiosError<ErrorResponse>(err)) {
        const message =
          err.response?.data?.message || 'Failed to resend verification code. Please try again.';
        setResendStatus({ message, isError: true });
      } else {
        setResendStatus({
          message: 'Network error. Could not resend verification code.',
          isError: true,
        });
      }
    } finally {
      setIsResending(false);
    }
  };

  return (
    <form
      onSubmit={handleSubmit(onSubmit)}
      noValidate
      className={className ?? 'flex flex-col gap-4 w-full'}
      aria-label="OTP verification form"
    >
      {formError && (
        <div
          role="alert"
          className="p-3.5 text-sm text-error bg-red-50 border border-red-200 rounded-default font-medium flex items-start gap-2"
        >
          <span className="shrink-0 font-bold" aria-hidden="true">
            !
          </span>
          <span>{formError}</span>
        </div>
      )}

      {successMessage && (
        <div
          role="status"
          className="p-3.5 text-sm text-emerald-800 bg-emerald-50 border border-emerald-200 rounded-default font-medium flex items-start gap-2"
        >
          <span className="shrink-0 font-bold" aria-hidden="true">
            ✓
          </span>
          <span>{successMessage}</span>
        </div>
      )}

      {resendStatus && (
        <div
          role={resendStatus.isError ? 'alert' : 'status'}
          className={`p-3 text-sm rounded-default font-medium flex items-start gap-2 ${
            resendStatus.isError
              ? 'text-error bg-red-50 border border-red-200'
              : 'text-emerald-800 bg-emerald-50 border border-emerald-200'
          }`}
        >
          <span>{resendStatus.message}</span>
        </div>
      )}

      <input type="hidden" {...register('email')} />

      <Input
        label="Verification Code"
        type="text"
        inputMode="numeric"
        pattern="[0-9]*"
        maxLength={6}
        placeholder="Enter 6-digit code"
        autoComplete="one-time-code"
        disabled={isSubmitting || Boolean(successMessage)}
        error={errors.otp?.message}
        {...register('otp')}
      />

      <Button
        type="submit"
        variant="primary"
        size="lg"
        loading={isSubmitting}
        disabled={isSubmitting || Boolean(successMessage)}
        className="w-full mt-1"
      >
        {isSubmitting ? 'Verifying code...' : 'Verify Code'}
      </Button>

      <div className="flex items-center justify-between pt-2 text-sm text-muted">
        <span>Didn't receive code?</span>
        <Button
          type="button"
          variant="ghost"
          size="sm"
          onClick={handleResendOtp}
          disabled={isResending || isSubmitting || Boolean(successMessage)}
          loading={isResending}
          className="text-primary hover:underline font-medium p-0 h-auto focus:ring-1 focus:ring-primary"
        >
          Resend OTP
        </Button>
      </div>
    </form>
  );
};
