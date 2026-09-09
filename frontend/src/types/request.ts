/**
 * Status of a team join request.
 */
export type JoinRequestStatus = 'PENDING' | 'ACCEPTED' | 'REJECTED';

/**
 * Request payload when applying to join a team.
 */
export interface ApplyJoinRequestRequest {
  message?: string | null;
}

/**
 * Response payload for a join request operation (e.g. create, accept, reject).
 */
export interface JoinRequestResponse {
  requestId: number;
  teamId: number;
  teamName: string;
  userId: number;
  userName: string;
  status: JoinRequestStatus;
  createdAt: string;
}

/**
 * Response payload representing an incoming join request viewed by a team leader.
 */
export interface IncomingJoinRequestResponse {
  requestId: number;
  userId: number;
  userName: string;
  profileImageUrl: string | null;
  message: string | null;
  status: JoinRequestStatus;
  createdAt: string;
}

/**
 * Response payload representing an outgoing join request submitted by the current user.
 */
export interface OutgoingJoinRequestResponse {
  requestId: number;
  teamId: number;
  teamName: string;
  status: JoinRequestStatus;
  createdAt: string;
}
