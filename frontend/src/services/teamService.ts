import api from '../lib/api';
import type {
  ApiResponse,
  CreateTeamRequest,
  Page,
  PageParams,
  TeamResponse,
  TeamSearchRequest,
  UpdateRecruitmentRequest,
  UpdateTeamRequest,
  UpdateWhatsAppRequest,
} from '../types';

/**
 * Service for team discovery, creation, leadership, and member recruitment.
 * Note: Team endpoints are wrapped in ApiResponse<T> envelopes.
 */
export const teamService = {
  /**
   * Fetch paginated list of all teams.
   */
  async getAllTeams(params?: PageParams): Promise<Page<TeamResponse>> {
    const response = await api.get<ApiResponse<Page<TeamResponse>>>('/teams', { params });
    return response.data.data;
  },

  /**
   * Fetch single team details by team ID.
   */
  async getTeamById(teamId: number): Promise<TeamResponse> {
    const response = await api.get<ApiResponse<TeamResponse>>(`/teams/${teamId}`);
    return response.data.data;
  },

  /**
   * Search and filter teams by keyword, event, required skill, recruitment status, or status.
   */
  async searchTeams(params?: TeamSearchRequest & PageParams): Promise<Page<TeamResponse>> {
    const response = await api.get<ApiResponse<Page<TeamResponse>>>('/teams/search', { params });
    return response.data.data;
  },

  /**
   * Create a new team for an event (creator becomes the leader).
   */
  async createTeam(payload: CreateTeamRequest): Promise<TeamResponse> {
    const response = await api.post<ApiResponse<TeamResponse>>('/teams', payload);
    return response.data.data;
  },

  /**
   * Update team metadata (name, description, maxMembers, recruitmentOpen, whatsappGroupLink).
   */
  async updateTeam(teamId: number, payload: UpdateTeamRequest): Promise<TeamResponse> {
    const response = await api.put<ApiResponse<TeamResponse>>(`/teams/${teamId}`, payload);
    return response.data.data;
  },

  /**
   * Delete a team (leader only).
   */
  async deleteTeam(teamId: number): Promise<void> {
    await api.delete<ApiResponse<void>>(`/teams/${teamId}`);
  },

  /**
   * Update team recruitment availability status.
   */
  async updateRecruitmentStatus(
    teamId: number,
    payload: UpdateRecruitmentRequest
  ): Promise<TeamResponse> {
    const response = await api.patch<ApiResponse<TeamResponse>>(
      `/teams/${teamId}/recruitment`,
      payload
    );
    return response.data.data;
  },

  /**
   * Update team WhatsApp group invite link.
   */
  async updateWhatsAppGroupLink(
    teamId: number,
    payload: UpdateWhatsAppRequest
  ): Promise<TeamResponse> {
    const response = await api.patch<ApiResponse<TeamResponse>>(
      `/teams/${teamId}/whatsapp`,
      payload
    );
    return response.data.data;
  },

  /**
   * Add a required skill to the team's skill requirements list.
   */
  async addRequiredSkill(teamId: number, skillId: number): Promise<TeamResponse> {
    const response = await api.post<ApiResponse<TeamResponse>>(
      `/teams/${teamId}/skills/${skillId}`
    );
    return response.data.data;
  },

  /**
   * Remove a required skill from the team's skill requirements list.
   */
  async removeRequiredSkill(teamId: number, skillId: number): Promise<TeamResponse> {
    const response = await api.delete<ApiResponse<TeamResponse>>(
      `/teams/${teamId}/skills/${skillId}`
    );
    return response.data.data;
  },
};
