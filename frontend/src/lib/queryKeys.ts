import type { EventFilterParams, PageParams, TeamSearchRequest } from '../types';

/**
 * Centralized, type-safe query key factory for all server-state resources.
 * Follows TanStack Query hierarchical key conventions to enable predictable cache invalidation.
 */
export const queryKeys = {
  // Authentication
  auth: {
    all: ['auth'] as const,
  },

  // Users & Profiles
  users: {
    all: ['users'] as const,
    me: () => [...queryKeys.users.all, 'me'] as const,
    detail: (userId: number) => [...queryKeys.users.all, 'detail', userId] as const,
    skills: (userId: number) => [...queryKeys.users.detail(userId), 'skills'] as const,
    search: (skillIds: number[], params?: PageParams) =>
      [...queryKeys.users.all, 'search', { skillIds, params }] as const,
  },

  // Skill catalog & mappings
  skills: {
    all: ['skills'] as const,
    list: () => [...queryKeys.skills.all, 'list'] as const,
    mySkills: () => [...queryKeys.skills.all, 'my-skills'] as const,
    userSkills: (userId: number) => [...queryKeys.skills.all, 'user-skills', userId] as const,
  },

  // Events
  events: {
    all: ['events'] as const,
    list: (params?: PageParams) => [...queryKeys.events.all, 'list', { params }] as const,
    detail: (eventId: number) => [...queryKeys.events.all, 'detail', eventId] as const,
    search: (params?: EventFilterParams & PageParams) =>
      [...queryKeys.events.all, 'search', { params }] as const,
    upcoming: (params?: PageParams) =>
      [...queryKeys.events.all, 'upcoming', { params }] as const,
    myEvents: (params?: PageParams) =>
      [...queryKeys.events.all, 'my-events', { params }] as const,
  },

  // Teams
  teams: {
    all: ['teams'] as const,
    list: (params?: PageParams) => [...queryKeys.teams.all, 'list', { params }] as const,
    detail: (teamId: number) => [...queryKeys.teams.all, 'detail', teamId] as const,
    search: (params?: TeamSearchRequest & PageParams) =>
      [...queryKeys.teams.all, 'search', { params }] as const,
  },

  // Join Requests
  requests: {
    all: ['requests'] as const,
    incoming: (teamId: number) => [...queryKeys.requests.all, 'incoming', teamId] as const,
    outgoing: () => [...queryKeys.requests.all, 'outgoing'] as const,
  },

  // Notifications
  notifications: {
    all: ['notifications'] as const,
    list: () => [...queryKeys.notifications.all, 'list'] as const,
    unreadCount: () => [...queryKeys.notifications.all, 'unread-count'] as const,
  },
} as const;
