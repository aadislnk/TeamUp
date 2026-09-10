import type { FieldError, FieldErrors, Resolver } from 'react-hook-form';
import type { ZodType } from 'zod';

/**
 * Lightweight, zero-dependency Zod resolver for React Hook Form.
 */
export function zodResolver<T extends Record<string, unknown>>(
  schema: ZodType<T>
): Resolver<T> {
  return async (values) => {
    const result = schema.safeParse(values);
    if (result.success) {
      return {
        values: result.data,
        errors: {},
      };
    }

    const errors: FieldErrors<T> = {};
    for (const issue of result.error.issues) {
      const fieldName = issue.path[0] as string;
      if (fieldName && !(errors as Record<string, FieldError | undefined>)[fieldName]) {
        (errors as Record<string, FieldError>)[fieldName] = {
          type: issue.code,
          message: issue.message,
        };
      }
    }

    return {
      values: {} as unknown as Record<string, never>,
      errors,
    };
  };
}

