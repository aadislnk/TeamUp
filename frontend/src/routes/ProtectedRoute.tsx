import React from 'react';
import { Navigate, Outlet, useLocation } from 'react-router-dom';
import { useAuth } from '../hooks';
import { Spinner } from '../components/ui';

/**
 * Guard for routes that require an authenticated user session.
 *
 * Prevents premature redirection while authentication status is 'loading'.
 * Redirects unauthenticated users to /login preserving target location in state.
 */
export const ProtectedRoute: React.FC = () => {
  const { status } = useAuth();
  const location = useLocation();

  if (status === 'loading') {
    return (
      <div className="min-h-screen flex items-center justify-center bg-background">
        <Spinner size="lg" className="text-primary" label="Verifying authentication status..." />
      </div>
    );
  }

  if (status === 'unauthenticated') {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  return <Outlet />;
};
