/**
 * Categorical type of notifications.
 */
export type NotificationType =
  | 'JOIN_REQUEST_RECEIVED'
  | 'JOIN_REQUEST_ACCEPTED'
  | 'JOIN_REQUEST_REJECTED'
  | 'JOIN_REQUEST_WITHDRAWN'
  | 'TEAM_INVITATION';

/**
 * Single user notification response item.
 */
export interface NotificationResponse {
  id: number;
  title: string;
  message: string;
  type: NotificationType;
  isRead: boolean;
  createdAt: string;
}

/**
 * Response payload containing count of unread notifications.
 */
export interface UnreadCountResponse {
  unreadCount: number;
}
