import React, { useId } from 'react';
import { cn } from '../../lib/utils';

export interface TextareaProps extends React.TextareaHTMLAttributes<HTMLTextAreaElement> {
  label?: string;
  error?: string;
  helperText?: string;
  containerClassName?: string;
}

export const Textarea = React.forwardRef<HTMLTextAreaElement, TextareaProps>(
  (
    {
      label,
      error,
      helperText,
      id,
      disabled,
      rows = 4,
      className,
      containerClassName,
      ...props
    },
    ref
  ) => {
    const generatedId = useId();
    const textareaId = id || (label ? generatedId : undefined);
    const errorId = textareaId ? `${textareaId}-error` : undefined;
    const helperId = textareaId ? `${textareaId}-helper` : undefined;

    const describedBy = error
      ? errorId
      : helperText
        ? helperId
        : undefined;

    return (
      <div className={cn('flex flex-col gap-1.5 w-full', containerClassName)}>
        {label && (
          <label
            htmlFor={textareaId}
            className={cn(
              'text-label text-foreground font-medium',
              disabled && 'opacity-60 cursor-not-allowed'
            )}
          >
            {label}
          </label>
        )}
        <textarea
          ref={ref}
          id={textareaId}
          rows={rows}
          disabled={disabled}
          aria-invalid={error ? 'true' : undefined}
          aria-describedby={describedBy}
          className={cn(
            'w-full px-3 py-2 text-sm text-foreground bg-surface border rounded-default transition-colors resize-y',
            'placeholder:text-muted',
            'focus:outline-none focus:ring-2 focus:ring-offset-1',
            error
              ? 'border-error focus:border-error focus:ring-error'
              : 'border-border focus:border-primary focus:ring-primary',
            'disabled:bg-slate-50 disabled:text-muted disabled:cursor-not-allowed disabled:opacity-60',
            className
          )}
          {...props}
        />
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

Textarea.displayName = 'Textarea';
