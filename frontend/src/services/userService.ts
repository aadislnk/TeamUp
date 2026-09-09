import api from '../lib/api';
import type {
  ApiResponse,
  MyProfileResponse,
  PublicProfileResponse,
  UpdateProfilePictureRequest,
  UpdateProfileRequest,
} from '../types';

/**
 * Service for user profile viewing and management.
 * Note: Profile endpoints are wrapped in ApiResponse<T> envelopes.
 */
export const userService = {
  /**
   * Fetch authenticated user's complete profile.
   */
  async getMyProfile(): Promise<MyProfileResponse> {
    const response = await api.get<ApiResponse<MyProfileResponse>>('/users/me');
    return response.data.data;
  },

  /**
   * Update profile fields for the authenticated user.
   */
  async updateProfile(payload: UpdateProfileRequest): Promise<MyProfileResponse> {
    const response = await api.put<ApiResponse<MyProfileResponse>>('/users/me', payload);
    return response.data.data;
  },

  /**
   * Fetch public profile for a specific user.
   */
  async getPublicProfile(userId: number): Promise<PublicProfileResponse> {
    const response = await api.get<ApiResponse<PublicProfileResponse>>(`/users/${userId}`);
    return response.data.data;
  },

  /**
   * Update profile picture / avatar selection for the authenticated user.
   */
  async updateProfilePicture(payload: UpdateProfilePictureRequest): Promise<MyProfileResponse> {
    const response = await api.put<ApiResponse<MyProfileResponse>>(
      '/users/me/profile-picture',
      payload
    );
    return response.data.data;
  },

  /**
   * Remove custom profile picture and revert to default.
   */
  async removeProfilePicture(): Promise<MyProfileResponse> {
    const response = await api.delete<ApiResponse<MyProfileResponse>>(
      '/users/me/profile-picture'
    );
    return response.data.data;
  },
};
