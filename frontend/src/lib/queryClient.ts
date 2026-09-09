import { QueryClient } from '@tanstack/react-query';

/**
 * Centralized TanStack Query client configuration.
 *
 * Configures sensible defaults:
 * - 2-minute stale time to balance fresh data with minimal network noise
 * - 10-minute cache retention (gcTime)
 * - Single retry on failure (prevents infinite/aggressive loops)
 * - Window focus refetching disabled to avoid unexpected UI shifts during navigation
 * - Automatic retry disabled for mutations
 */
export const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 1000 * 60 * 2, // 2 minutes
      gcTime: 1000 * 60 * 10, // 10 minutes
      retry: 1,
      refetchOnWindowFocus: false,
      refetchOnReconnect: true,
    },
    mutations: {
      retry: false,
    },
  },
});
