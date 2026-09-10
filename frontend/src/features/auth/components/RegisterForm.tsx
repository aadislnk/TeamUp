import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate, Link } from 'react-router-dom';
import axios from 'axios';
import { Input, Select, Button } from '../../../components/ui';
import { authService } from '../../../services';
import { zodResolver } from '../../../lib/form';
import { registerSchema, type RegisterFormData } from '../schemas/registerSchema';
import type { ErrorResponse, RegisterResponse } from '../../../types';

export interface RegisterFormProps {
  /** Optional callback invoked on successful registration */
  onSuccess?: (response: RegisterResponse) => void;
  className?: string;
}

/**
 * Registration form component for new student onboarding.
 *
 * Handles client-side validation via Zod, submission via authService.register(),
 * field and form-level error rendering, and transition to OTP verification.
 */
export const RegisterForm: React.FC<RegisterFormProps> = ({ onSuccess, className }) => {
  const navigate = useNavigate();
  const [formError, setFormError] = useState<string | null>(null);

  const {
    register,
    handleSubmit,
    setError,
    formState: { errors, isSubmitting },
  } = useForm<RegisterFormData>({
    resolver: zodResolver(registerSchema),
    mode: 'onTouched',
    defaultValues: {
      fullName: '',
      email: '',
      college: '',
      academicYear: undefined,
      gender: undefined,
      password: '',
      confirmPassword: '',
    },
  });

  const onSubmit = async (data: RegisterFormData) => {
    setFormError(null);

    try {
      const response = await authService.register(data);

      if (onSuccess) {
        onSuccess(response);
      } else {
        navigate('/verify-otp', {
          state: { email: data.email },
        });
      }
    } catch (err: unknown) {
      if (axios.isAxiosError<ErrorResponse>(err)) {
        const errorData = err.response?.data;

        // Map field-level validation errors from Spring Boot backend if available
        if (errorData?.errors && errorData.errors.length > 0) {
          let mappedField = false;

          for (const fieldErr of errorData.errors) {
            if (fieldErr.field && fieldErr.message) {
              const fieldName = fieldErr.field as keyof RegisterFormData;
              if (
                [
                  'fullName',
                  'email',
                  'college',
                  'academicYear',
                  'gender',
                  'password',
                  'confirmPassword',
                ].includes(fieldName)
              ) {
                setError(fieldName, {
                  type: 'server',
                  message: fieldErr.message,
                });
                mappedField = true;
              }
            }
          }

          if (errorData.message && !mappedField) {
            setFormError(errorData.message);
          }
        } else if (errorData?.message) {
          setFormError(errorData.message);
        } else if (err.response?.status === 409) {
          setFormError('An account with this email already exists.');
        } else {
          setFormError('Registration failed. Please check your information and try again.');
        }
      } else {
        setFormError('Unable to connect to the server. Please check your network and try again.');
      }
    }
  };

  return (
    <form
      onSubmit={handleSubmit(onSubmit)}
      noValidate
      className={className ?? 'flex flex-col gap-4 w-full'}
      aria-label="Registration form"
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

      <Input
        label="Full Name"
        type="text"
        placeholder="e.g. Alex Rivera"
        autoComplete="name"
        disabled={isSubmitting}
        error={errors.fullName?.message}
        {...register('fullName')}
      />

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
        label="College / University"
        type="text"
        placeholder="e.g. Stanford University"
        autoComplete="organization"
        disabled={isSubmitting}
        error={errors.college?.message}
        {...register('college')}
      />

      <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
        <Select
          label="Academic Year"
          disabled={isSubmitting}
          error={errors.academicYear?.message}
          {...register('academicYear')}
        >
          <option value="">Select your year</option>
          <option value="FIRST_YEAR">First Year</option>
          <option value="SECOND_YEAR">Second Year</option>
          <option value="THIRD_YEAR">Third Year</option>
          <option value="FOURTH_YEAR">Fourth Year</option>
          <option value="GRADUATED">Graduated</option>
        </Select>

        <Select
          label="Gender"
          disabled={isSubmitting}
          error={errors.gender?.message}
          {...register('gender')}
        >
          <option value="">Select gender</option>
          <option value="MALE">Male</option>
          <option value="FEMALE">Female</option>
          <option value="OTHER">Other</option>
        </Select>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
        <Input
          label="Password"
          type="password"
          placeholder="Min. 8 characters"
          autoComplete="new-password"
          disabled={isSubmitting}
          error={errors.password?.message}
          {...register('password')}
        />

        <Input
          label="Confirm Password"
          type="password"
          placeholder="Re-enter password"
          autoComplete="new-password"
          disabled={isSubmitting}
          error={errors.confirmPassword?.message}
          {...register('confirmPassword')}
        />
      </div>

      <Button
        type="submit"
        variant="primary"
        size="lg"
        loading={isSubmitting}
        disabled={isSubmitting}
        className="w-full mt-2"
      >
        {isSubmitting ? 'Creating account...' : 'Create Account'}
      </Button>

      <div className="text-center text-sm text-muted pt-1">
        Already have an account?{' '}
        <Link
          to="/login"
          className="text-primary font-medium hover:underline focus:outline-none focus:ring-1 focus:ring-primary rounded-sm"
        >
          Log in
        </Link>
      </div>
    </form>
  );
};
