/**
 * Event conduction mode.
 */
export type EventMode = 'ONLINE' | 'OFFLINE' | 'HYBRID';

/**
 * Event lifecycle status.
 */
export type EventStatus = 'UPCOMING' | 'ONGOING' | 'COMPLETED' | 'CANCELLED';

/**
 * Categorical type of the event.
 */
export type EventType =
  | 'HACKATHON'
  | 'IDEATHON'
  | 'WORKSHOP'
  | 'WEBINAR'
  | 'BOOTCAMP'
  | 'SEMINAR'
  | 'COMPETITION'
  | 'OTHER';

/**
 * Request payload to create a new event.
 */
export interface CreateEventRequest {
  title: string;
  description?: string | null;
  organizer: string;
  location?: string | null;
  eventUrl?: string | null;
  registrationUrl?: string | null;
  bannerUrl?: string | null;
  mode: EventMode;
  status: EventStatus;
  type: EventType;
  minTeamSize: number;
  maxTeamSize: number;
  registrationStart: string;
  registrationEnd: string;
  eventStart: string;
  eventEnd: string;
}

/**
 * Request payload to update an existing event.
 */
export interface UpdateEventRequest {
  title: string;
  description?: string | null;
  organizer: string;
  location?: string | null;
  eventUrl?: string | null;
  registrationUrl?: string | null;
  bannerUrl?: string | null;
  mode: EventMode;
  status: EventStatus;
  type: EventType;
  minTeamSize: number;
  maxTeamSize: number;
  registrationStart: string;
  registrationEnd: string;
  eventStart: string;
  eventEnd: string;
}

/**
 * Query parameters for filtering and searching events.
 */
export interface EventFilterParams {
  keyword?: string;
  type?: EventType;
  mode?: EventMode;
  status?: EventStatus;
  registrationOpen?: boolean;
}

/**
 * Complete event details response.
 */
export interface EventResponse {
  id: number;
  title: string;
  description: string | null;
  organizer: string;
  location: string | null;
  eventUrl: string | null;
  registrationUrl: string | null;
  bannerUrl: string | null;
  ownerId: number;
  ownerName: string;
  ownerProfileImage: string | null;
  type: EventType;
  mode: EventMode;
  status: EventStatus;
  registrationStart: string;
  registrationEnd: string;
  eventStart: string;
  eventEnd: string;
  registrationOpen: boolean;
  minTeamSize: number;
  maxTeamSize: number;
  createdAt: string;
  updatedAt: string;
}

/**
 * Compact event summary response used in event listings and upcoming feeds.
 */
export interface EventSummaryResponse {
  id: number;
  title: string;
  organizer: string;
  location: string | null;
  bannerUrl: string | null;
  ownerId: number;
  ownerName: string;
  ownerProfileImage: string | null;
  type: EventType;
  mode: EventMode;
  status: EventStatus;
  eventStart: string;
  eventEnd: string;
  registrationOpen: boolean;
}
