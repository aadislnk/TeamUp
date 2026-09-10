import { z } from 'zod';
import type { AcademicYear, Gender } from '../../../types';

const academicYearValues: [AcademicYear, ...AcademicYear[]] = [
  'FIRST_YEAR',
  'SECOND_YEAR',
  'THIRD_YEAR',
  'FOURTH_YEAR',
  'GRADUATED',
];

const genderValues: [Gender, ...Gender[]] = ['MALE', 'FEMALE', 'OTHER'];

/**
 * Validation schema for the student registration form.
 * Mirrors constraints defined in RegisterRequest.
 */
export const registerSchema = z
  .object({
    fullName: z
      .string()
      .trim()
      .min(2, 'Full name must be at least 2 characters')
      .max(100, 'Full name cannot exceed 100 characters'),
    email: z
      .string()
      .trim()
      .min(1, 'Email address is required')
      .email('Please enter a valid email address'),
    college: z
      .string()
      .trim()
      .min(2, 'College/University name is required')
      .max(150, 'College name cannot exceed 150 characters'),
    academicYear: z.enum(academicYearValues, {
      message: 'Please select your academic year',
    }),
    gender: z.enum(genderValues, {
      message: 'Please select your gender',
    }),
    password: z
      .string()
      .min(8, 'Password must be at least 8 characters')
      .max(100, 'Password cannot exceed 100 characters'),
    confirmPassword: z
      .string()
      .min(1, 'Please confirm your password'),
  })
  .refine((data) => data.password === data.confirmPassword, {
    message: 'Passwords do not match',
    path: ['confirmPassword'],
  });

export type RegisterFormData = z.infer<typeof registerSchema>;
