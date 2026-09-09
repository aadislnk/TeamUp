import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { queryKeys } from '../lib/queryKeys';
import { skillService } from '../services';
import type { AddSkillsRequest } from '../types';

/**
 * Hook to fetch the master skill catalog.
 */
export function useSkills() {
  return useQuery({
    queryKey: queryKeys.skills.list(),
    queryFn: () => skillService.getAllSkills(),
  });
}

/**
 * Hook to fetch the authenticated user's selected skills.
 */
export function useMySkills() {
  return useQuery({
    queryKey: queryKeys.skills.mySkills(),
    queryFn: () => skillService.getMySkills(),
  });
}

/**
 * Hook to add skills to current user profile with proper cache invalidation.
 */
export function useAddSkills() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (payload: AddSkillsRequest) => skillService.addSkills(payload),
    onSuccess: () => {
      // Invalidate both current user skills and profile caches
      queryClient.invalidateQueries({ queryKey: queryKeys.skills.mySkills() });
      queryClient.invalidateQueries({ queryKey: queryKeys.users.me() });
    },
  });
}
