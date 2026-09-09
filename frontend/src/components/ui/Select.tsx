import React, { useId } from 'react';
import { cn } from '../../lib/utils';

export interface SelectProps extends React.SelectHTMLAttributes<HTMLSelectElement> {
  label?: string;
  error?: string;
  helperText?: string;
  containerClassName?: string;
}

export const Select = React.forwardRef<HTMLSelectElement, SelectProps>(
  (
    {
      label,
      error,
      helperText,
      id,
      disabled,
      className,
      containerClassName,
      children,
      ...props
    },
    ref
  ) => {
    const generatedId = useId();
    const selectId = id || (label ? generatedId : undefined);
    const errorId = selectId ? `${selectId}-error` : undefined;
    const helperId = selectId ? `${selectId}-helper` : undefined;

    const describedBy = error
      ? errorId
      : helperText
        ? helperId
        : undefined;

    return (
      <div className={cn('flex flex-col gap-1.5 w-full', containerClassName)}>
        {label && (
          <label
            htmlFor={selectId}
            className={cn(
              'text-label text-foreground font-medium',
              disabled && 'opacity-60 cursor-not-allowed'
            )}
          >
            {label}
          </label>
        )}
        <select
          ref={ref}
          id={selectId}
          disabled={disabled}
          aria-invalid={error ? 'true' : undefined}
          aria-describedby={describedBy}
          className={cn(
            'w-full px-3 py-2 text-sm text-foreground bg-surface border rounded-default transition-colors cursor-pointer',
            'focus:outline-none focus:ring-2 focus:ring-offset-1',
            error
              ? 'border-error focus:border-error focus:ring-error'
              : 'border-border focus:border-primary focus:ring-primary',
            'disabled:bg-slate-50 disabled:text-muted disabled:cursor-not-allowed disabled:opacity-60',
            className
          )}
          {...props}
        >
          {children}
        </select>
        {error && (
          <p id={errorId} role="alert" className="text-xs text-error font-medium">
            {error}
          </p>
        )}
        {!error && helperText && (
          <p id={helperId} className="text-xs text-muted">
            {helperText}
          </p>
        )}
      </div>
    );
  }
);

Select.displayName = 'Select';
