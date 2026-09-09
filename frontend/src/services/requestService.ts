import api from '../lib/api';
import type {
  ApiResponse,
  ApplyJoinRequestRequest,
  IncomingJoinRequestResponse,
  JoinRequestResponse,
  OutgoingJoinRequestResponse,
} from '../types';

/**
 * Service for team join requests, applications, and leadership decisions.
 * Note: Join request endpoints are wrapped in ApiResponse<T> envelopes.
 */
export const requestService = {
  /**
   * Apply to join a team with an optional intro message.
   */
  async applyToTeam(
    teamId: number,
    payload: ApplyJoinRequestRequest
  ): Promise<JoinRequestResponse> {
    const response = await api.post<ApiResponse<JoinRequestResponse>>(
      `/teams/${teamId}/apply`,
      payload
    );
    return response.data.data;
  },

  /**
   * Withdraw a pending outgoing join request.
   */
  async withdrawRequest(requestId: number): Promise<void> {
    await api.delete<ApiResponse<void>>(`/requests/${requestId}`);
  },

  /**
   * Accept an incoming join request (leader only).
   */
  async acceptRequest(requestId: number): Promise<JoinRequestResponse> {
    const response = await api.patch<ApiResponse<JoinRequestResponse>>(
      `/requests/${requestId}/accept`
    );
    return response.data.data;
  },

  /**
   * Reject an incoming join request (leader only).
   */
  async rejectRequest(requestId: number): Promise<JoinRequestResponse> {
    const response = await api.patch<ApiResponse<JoinRequestResponse>>(
      `/requests/${requestId}/reject`
    );
    return response.data.data;
  },

  /**
   * Fetch incoming join requests submitted to a specific team (leader only).
   */
  async getIncomingRequests(teamId: number): Promise<IncomingJoinRequestResponse[]> {
    const response = await api.get<ApiResponse<IncomingJoinRequestResponse[]>>(
      `/teams/${teamId}/requests`
    );
    return response.data.data;
  },

  /**
   * Fetch outgoing join requests submitted by the authenticated user.
   */
  async getOutgoingRequests(): Promise<OutgoingJoinRequestResponse[]> {
    const response = await api.get<ApiResponse<OutgoingJoinRequestResponse[]>>(
      '/users/me/requests'
    );
    return response.data.data;
  },
};
