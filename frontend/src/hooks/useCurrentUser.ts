import { useQuery } from '@tanstack/react-query';
import { getAccessToken } from '../lib/token';
import { queryKeys } from '../lib/queryKeys';
import { userService } from '../services';

/**
 * Hook to fetch the currently authenticated user's profile.
 * Only executes when a JWT access token is present in storage.
 */
export function useCurrentUser() {
  const token = getAccessToken();

  return useQuery({
    queryKey: queryKeys.users.me(),
    queryFn: () => userService.getMyProfile(),
    enabled: !!token,
  });
}
