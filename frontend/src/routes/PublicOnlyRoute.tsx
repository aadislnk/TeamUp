import React from 'react';
import { Navigate, Outlet, useLocation } from 'react-router-dom';
import { useAuth } from '../hooks';
import { Spinner } from '../components/ui';

interface LocationState {
  from?: Location;
}

/**
 * Guard for public authentication routes (e.g. /login, /register, /verify-otp).
 *
 * Prevents premature redirection while authentication status is 'loading'.
 * Redirects authenticated users away from authentication pages to target or default route.
 */
export const PublicOnlyRoute: React.FC = () => {
  const { status } = useAuth();
  const location = useLocation();

  if (status === 'loading') {
    return (
      <div className="min-h-screen flex items-center justify-center bg-background">
        <Spinner size="lg" className="text-primary" label="Verifying authentication status..." />
      </div>
    );
  }

  if (status === 'authenticated') {
    const state = location.state as LocationState | null;
    const destination = state?.from?.pathname || '/';
    return <Navigate to={destination} replace />;
  }

  return <Outlet />;
};
