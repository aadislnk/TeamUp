import React, {
  useCallback,
  useEffect,
  useMemo,
  useState,
  type ReactNode,
} from 'react';
import { useQuery, useQueryClient } from '@tanstack/react-query';
import type { AxiosError } from 'axios';
import { getAccessToken, removeAccessToken, setAccessToken } from '../../lib/token';
import { queryKeys } from '../../lib/queryKeys';
import { userService } from '../../services';
import type { AuthContextValue, AuthStatus } from '../../types';
import { AuthContext } from './AuthContext';

export interface AuthProviderProps {
  children: ReactNode;
}

/**
 * Centralized Authentication Provider.
 *
 * Responsibilities:
 * - Reads access token presence from centralized token utility.
 * - Fetches authenticated user profile via TanStack Query (/users/me).
 * - Distinguishes between 'loading', 'authenticated', and 'unauthenticated' states.
 * - Handles token revocation on HTTP 401/403 responses without swallowing errors.
 * - Manages session initialization, login, and selective cache clearing on logout.
 */
export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [token, setToken] = useState<string | null>(() => getAccessToken());
  const queryClient = useQueryClient();

  const {
    data: user = null,
    isLoading: isQueryLoading,
    isError,
    error,
  } = useQuery({
    queryKey: queryKeys.users.me(),
    queryFn: () => userService.getMyProfile(),
    enabled: !!token,
    retry: (failureCount, queryError: AxiosError | Error) => {
      const httpStatus = (queryError as AxiosError)?.response?.status;
      if (httpStatus === 401 || httpStatus === 403) {
        return false;
      }
      return failureCount < 1;
    },
  });

  const isAuthError =
    isError &&
    ((error as AxiosError)?.response?.status === 401 ||
      (error as AxiosError)?.response?.status === 403);

  // Sync token removal and query cache reset when backend reports 401/403 unauthorized
  useEffect(() => {
    if (isAuthError) {
      removeAccessToken();
      queryClient.setQueryData(queryKeys.users.me(), null);
    }
  }, [isAuthError, queryClient]);

  // Compute clean discriminated authentication status
  const status: AuthStatus = useMemo(() => {
    if (!token || isAuthError) {
      return 'unauthenticated';
    }
    if (isQueryLoading) {
      return 'loading';
    }
    if (user) {
      return 'authenticated';
    }
    if (isError) {
      return 'unauthenticated';
    }
    return 'loading';
  }, [token, isAuthError, isQueryLoading, user, isError]);

  const isLoading = status === 'loading';
  const isAuthenticated = status === 'authenticated';
  const isUnauthenticated = status === 'unauthenticated';

  /**
   * Log in user by storing the access token, updating token state, and triggering profile fetch.
   */
  const login = useCallback(
    async (accessToken: string): Promise<void> => {
      setAccessToken(accessToken);
      setToken(accessToken);
      await queryClient.invalidateQueries({ queryKey: queryKeys.users.me() });
    },
    [queryClient]
  );

  /**
   * Log out user: clears token from storage, resets auth state, and purges user-specific caches.
   */
  const logout = useCallback((): void => {
    removeAccessToken();
    setToken(null);

    // Reset user profile query data
    queryClient.setQueryData(queryKeys.users.me(), null);

    // Selectively remove user-scoped cached resources without destroying global catalog cache
    queryClient.removeQueries({ queryKey: queryKeys.users.all });
    queryClient.removeQueries({ queryKey: queryKeys.skills.mySkills() });
    queryClient.removeQueries({ queryKey: queryKeys.events.myEvents() });
    queryClient.removeQueries({ queryKey: queryKeys.requests.all });
    queryClient.removeQueries({ queryKey: queryKeys.notifications.all });
  }, [queryClient]);

  const contextValue = useMemo<AuthContextValue>(
    () => ({
      user: status === 'authenticated' ? user : null,
      status,
      isLoading,
      isAuthenticated,
      isUnauthenticated,
      login,
      logout,
    }),
    [user, status, isLoading, isAuthenticated, isUnauthenticated, login, logout]
  );

  return <AuthContext.Provider value={contextValue}>{children}</AuthContext.Provider>;
};
