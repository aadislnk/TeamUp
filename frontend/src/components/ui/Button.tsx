import React from 'react';
import { cn } from '../../lib/utils';
import { Spinner } from './Spinner';

export type ButtonVariant = 'primary' | 'secondary' | 'outline' | 'destructive' | 'ghost';
export type ButtonSize = 'sm' | 'md' | 'lg';

export interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: ButtonVariant;
  size?: ButtonSize;
  loading?: boolean;
}

const variantClasses: Record<ButtonVariant, string> = {
  primary:
    'bg-primary text-primary-foreground hover:bg-blue-700 active:bg-blue-800 disabled:bg-primary/50',
  secondary:
    'bg-secondary text-secondary-foreground hover:bg-teal-800 active:bg-teal-900 disabled:bg-secondary/50',
  outline:
    'border border-border bg-surface text-foreground hover:bg-slate-50 active:bg-slate-100 disabled:bg-surface disabled:text-muted',
  destructive:
    'bg-error text-white hover:bg-red-700 active:bg-red-800 disabled:bg-error/50',
  ghost:
    'bg-transparent text-foreground hover:bg-slate-100 active:bg-slate-200 disabled:bg-transparent disabled:text-muted',
};

const sizeClasses: Record<ButtonSize, string> = {
  sm: 'px-3 py-1.5 text-xs font-medium gap-1.5',
  md: 'px-4 py-2 text-sm font-medium gap-2',
  lg: 'px-5 py-2.5 text-base font-medium gap-2.5',
};

const spinnerSizeMap: Record<ButtonSize, 'sm' | 'md'> = {
  sm: 'sm',
  md: 'sm',
  lg: 'md',
};

export const Button = React.forwardRef<HTMLButtonElement, ButtonProps>(
  (
    {
      variant = 'primary',
      size = 'md',
      loading = false,
      disabled = false,
      type = 'button',
      className,
      children,
      ...props
    },
    ref
  ) => {
    const isDisabled = disabled || loading;

    return (
      <button
        ref={ref}
        type={type}
        disabled={isDisabled}
        aria-busy={loading ? 'true' : undefined}
        className={cn(
          'inline-flex items-center justify-center rounded-default font-medium transition-colors cursor-pointer select-none',
          'focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-primary focus-visible:ring-offset-2',
          'disabled:cursor-not-allowed disabled:opacity-60',
          variantClasses[variant],
          sizeClasses[size],
          className
        )}
        {...props}
      >
        {loading && <Spinner size={spinnerSizeMap[size]} className="text-current shrink-0" />}
        {children}
      </button>
    );
  }
);

Button.displayName = 'Button';
