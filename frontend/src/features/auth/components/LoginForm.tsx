import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useLocation, Link } from 'react-router-dom';
import axios from 'axios';
import { Input, Button } from '../../../components/ui';
import { authService } from '../../../services';
import { zodResolver } from '../../../lib/form';
import { useAuth } from '../../../hooks';
import { loginSchema, type LoginFormData } from '../schemas/loginSchema';
import type { ErrorResponse, LoginResponse } from '../../../types';

interface LocationState {
  emailVerified?: boolean;
  email?: string;
}

export interface LoginFormProps {
  /** Optional callback invoked on successful login and session establishment */
  onSuccess?: (response: LoginResponse) => void;
  className?: string;
}

/**
 * Login Form component for student authentication.
 *
 * Handles client-side validation via Zod, authenticates via authService.login(),
 * integrates with AuthProvider.login(accessToken), handles router state from OTP verification,
 * and renders field and form-level error feedback.
 */
export const LoginForm: React.FC<LoginFormProps> = ({ onSuccess, className }) => {
  const location = useLocation();
  const { login } = useAuth();
  const [formError, setFormError] = useState<string | null>(null);

  const state = location.state as LocationState | null;
  const initialEmail = state?.email ?? '';
  const showVerifiedNotice = Boolean(state?.emailVerified);

  const {
    register,
    handleSubmit,
    setError,
    formState: { errors, isSubmitting },
  } = useForm<LoginFormData>({
    resolver: zodResolver(loginSchema),
    mode: 'onTouched',
    defaultValues: {
      email: initialEmail,
      password: '',
    },
  });

  const onSubmit = async (data: LoginFormData) => {
    setFormError(null);

    try {
      const response = await authService.login(data);

      // Establish authenticated session in AuthProvider via centralized login method
      await login(response.accessToken);

      if (onSuccess) {
        onSuccess(response);
      }
    } catch (err: unknown) {
      if (axios.isAxiosError<ErrorResponse>(err)) {
        const errorData = err.response?.data;
        const status = err.response?.status;

        if (status === 401) {
          setFormError('Invalid email address or password. Please check your credentials.');
        } else if (errorData?.errors && errorData.errors.length > 0) {
          let mappedField = false;

          for (const fieldErr of errorData.errors) {
            if (fieldErr.field && fieldErr.message) {
              const fieldName = fieldErr.field as keyof LoginFormData;
              if (['email', 'password'].includes(fieldName)) {
                setError(fieldName, {
                  type: 'server',
                  message: fieldErr.message,
                });
                mappedField = true;
              }
            }
          }

          if (!mappedField && errorData.message) {
            setFormError(errorData.message);
          }
        } else if (errorData?.message) {
          setFormError(errorData.message);
        } else {
          setFormError('Login failed. Please check your credentials and try again.');
        }
      } else {
        setFormError('Unable to connect to the server. Please check your network connection.');
      }
    }
  };

  return (
    <form
      onSubmit={handleSubmit(onSubmit)}
      noValidate
      className={className ?? 'flex flex-col gap-4 w-full'}
      aria-label="Login form"
    >
      {showVerifiedNotice && (
        <div
          role="status"
          className="p-3.5 text-sm text-emerald-800 bg-emerald-50 border border-emerald-200 rounded-default font-medium flex items-start gap-2"
        >
          <span className="shrink-0 font-bold" aria-hidden="true">
            ✓
          </span>
          <span>Email address verified successfully! Please log in with your credentials.</span>
        </div>
      )}

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

      <Input
        label="Email Address"
        type="email"
        placeholder="e.g. alex@university.edu"
        autoComplete="email"
        disabled={isSubmitting}
        error={errors.email?.message}
        {...register('email')}
      />

      <Input
        label="Password"
        type="password"
        placeholder="Enter your password"
        autoComplete="current-password"
        disabled={isSubmitting}
        error={errors.password?.message}
        {...register('password')}
      />

      <Button
        type="submit"
        variant="primary"
        size="lg"
        loading={isSubmitting}
        disabled={isSubmitting}
        className="w-full mt-2"
      >
        {isSubmitting ? 'Signing in...' : 'Sign In'}
      </Button>

      <div className="text-center text-sm text-muted pt-1">
        Don't have an account?{' '}
        <Link
          to="/register"
          className="text-primary font-medium hover:underline focus:outline-none focus:ring-1 focus:ring-primary rounded-sm"
        >
          Sign up
        </Link>
      </div>
    </form>
  );
};
