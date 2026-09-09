import api from '../lib/api';
import type {
  ApiResponse,
  NotificationResponse,
  UnreadCountResponse,
} from '../types';

/**
 * Service for notification retrieval and status updates.
 * Note: Notification endpoints are wrapped in ApiResponse<T> envelopes.
 */
export const notificationService = {
  /**
   * Fetch all notifications for the authenticated user.
   */
  async getMyNotifications(): Promise<NotificationResponse[]> {
    const response = await api.get<ApiResponse<NotificationResponse[]>>(
      '/users/me/notifications'
    );
    return response.data.data;
  },

  /**
   * Fetch the unread notification count for the authenticated user.
   */
  async getUnreadCount(): Promise<UnreadCountResponse> {
    const response = await api.get<ApiResponse<UnreadCountResponse>>(
      '/users/me/notifications/unread-count'
    );
    return response.data.data;
  },

  /**
   * Mark a single notification as read by ID.
   */
  async markAsRead(id: number): Promise<void> {
    await api.patch<ApiResponse<void>>(`/notifications/${id}/read`);
  },

  /**
   * Mark all notifications for the authenticated user as read.
   */
  async markAllAsRead(): Promise<void> {
    await api.patch<ApiResponse<void>>('/notifications/read-all');
  },
};
