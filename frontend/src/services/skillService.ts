import api from '../lib/api';
import type {
  AddSkillsRequest,
  ApiResponse,
  Page,
  PageParams,
  SkillResponse,
  UserSearchResponse,
  UserSkillResponse,
} from '../types';

/**
 * Query parameters for searching users by skills.
 */
export interface UserSkillSearchParams extends PageParams {
  skillIds: number[];
}

/**
 * Service for master skill catalog, user skill mapping, and skill-based user discovery.
 * Note: Skill endpoints are wrapped in ApiResponse<T> envelopes.
 */
export const skillService = {
  /**
   * Fetch all skills from the master catalog.
   */
  async getAllSkills(): Promise<SkillResponse[]> {
    const response = await api.get<ApiResponse<SkillResponse[]>>('/skills');
    return response.data.data;
  },

  /**
   * Fetch skills selected by the authenticated user.
   */
  async getMySkills(): Promise<UserSkillResponse[]> {
    const response = await api.get<ApiResponse<UserSkillResponse[]>>('/users/me/skills');
    return response.data.data;
  },

  /**
   * Add skills to the authenticated user's profile.
   */
  async addSkills(payload: AddSkillsRequest): Promise<UserSkillResponse[]> {
    const response = await api.post<ApiResponse<UserSkillResponse[]>>(
      '/users/me/skills',
      payload
    );
    return response.data.data;
  },

  /**
   * Remove a skill from the authenticated user's profile.
   */
  async removeSkill(skillId: number): Promise<void> {
    await api.delete<ApiResponse<void>>(`/users/me/skills/${skillId}`);
  },

  /**
   * Fetch skills selected by a specific user.
   */
  async getUserSkills(userId: number): Promise<UserSkillResponse[]> {
    const response = await api.get<ApiResponse<UserSkillResponse[]>>(
      `/users/${userId}/skills`
    );
    return response.data.data;
  },

  /**
   * Search and discover users possessing specific skills.
   */
  async searchUsers(
    skillIds: number[],
    pageParams?: PageParams
  ): Promise<Page<UserSearchResponse>> {
    const response = await api.get<ApiResponse<Page<UserSearchResponse>>>('/users/search', {
      params: {
        skillIds: skillIds.join(','),
        ...pageParams,
      },
    });
    return response.data.data;
  },
};
