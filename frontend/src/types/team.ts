import type { SkillResponse } from './user';

/**
 * Team status representing member availability.
 */
export type TeamStatus = 'OPEN' | 'FULL' | 'CLOSED';

/**
 * Team member summary within a team details view.
 */
export interface MemberResponse {
  userId: number;
  fullName: string;
  profileImageUrl: string | null;
  leader: boolean;
}

/**
 * Request payload to create a new team for an event.
 */
export interface CreateTeamRequest {
  name: string;
  description?: string | null;
  maxMembers: number;
  recruitmentOpen: boolean;
  whatsappGroupLink?: string | null;
  eventId: number;
}

/**
 * Request payload to update team metadata.
 */
export interface UpdateTeamRequest {
  name: string;
  description?: string | null;
  maxMembers: number;
  recruitmentOpen: boolean;
  whatsappGroupLink?: string | null;
}

/**
 * Query parameters / body for searching and filtering teams.
 */
export interface TeamSearchRequest {
  keyword?: string | null;
  eventId?: number | null;
  skillId?: number | null;
  recruitmentOpen?: boolean | null;
  status?: TeamStatus | null;
}

/**
 * Request payload to update a team's recruitment status.
 */
export interface UpdateRecruitmentRequest {
  recruitmentOpen: boolean;
}

/**
 * Request payload to update a team's WhatsApp group link.
 */
export interface UpdateWhatsAppRequest {
  whatsappGroupLink?: string | null;
}

/**
 * Complete team details response including members and required skills.
 */
export interface TeamResponse {
  id: number;
  name: string;
  description: string | null;
  currentMembers: number;
  maxMembers: number;
  recruitmentOpen: boolean;
  whatsappGroupLink: string | null;
  status: TeamStatus;
  leaderId: number;
  leaderName: string;
  eventId: number;
  eventTitle: string;
  requiredSkills: SkillResponse[];
  members: MemberResponse[];
  createdAt: string;
  updatedAt: string;
}

/**
 * Compact team summary response for list views.
 */
export interface TeamSummaryResponse {
  id: number;
  name: string;
  currentMembers: number;
  maxMembers: number;
  recruitmentOpen: boolean;
  status: TeamStatus;
  leaderName: string;
  eventTitle: string;
}

/**
 * Leader dashboard overview response for managing a team.
 */
export interface LeaderDashboardResponse {
  team: TeamResponse;
  pendingRequestsCount: number;
  acceptedMembers: number;
  recruitmentOpen: boolean;
}
