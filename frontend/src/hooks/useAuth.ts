import { useContext } from 'react';
import { AuthContext } from '../features/auth';
import type { AuthContextValue } from '../types';

/**
 * Hook to consume the current authentication state and actions.
 *
 * @throws {Error} If called outside of an `<AuthProvider>` tree.
 */
export function useAuth(): AuthContextValue {
  const context = useContext(AuthContext);

  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }

  return context;
}
