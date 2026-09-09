import api from '../lib/api';
import type {
  CreateEventRequest,
  EventFilterParams,
  EventResponse,
  EventSummaryResponse,
  Page,
  PageParams,
  UpdateEventRequest,
} from '../types';

/**
 * Service for public event discovery and event management.
 * Note: Event endpoints return raw Page/DTO objects directly rather than ApiResponse<T> envelopes.
 */
export const eventService = {
  /**
   * Fetch paginated list of all public events.
   */
  async getAllEvents(params?: PageParams): Promise<Page<EventResponse>> {
    const response = await api.get<Page<EventResponse>>('/events', { params });
    return response.data;
  },

  /**
   * Fetch single event details by event ID.
   */
  async getEventById(eventId: number): Promise<EventResponse> {
    const response = await api.get<EventResponse>(`/events/${eventId}`);
    return response.data;
  },

  /**
   * Search and filter events with keyword, type, mode, status, or registration status.
   */
  async searchEvents(
    params?: EventFilterParams & PageParams
  ): Promise<Page<EventSummaryResponse>> {
    const response = await api.get<Page<EventSummaryResponse>>('/events/search', { params });
    return response.data;
  },

  /**
   * Fetch upcoming events feed.
   */
  async getUpcomingEvents(params?: PageParams): Promise<Page<EventSummaryResponse>> {
    const response = await api.get<Page<EventSummaryResponse>>('/events/upcoming', { params });
    return response.data;
  },

  /**
   * Create a new event owned by the authenticated user.
   */
  async createEvent(payload: CreateEventRequest): Promise<EventResponse> {
    const response = await api.post<EventResponse>('/events', payload);
    return response.data;
  },

  /**
   * Update an existing event owned by the authenticated user.
   */
  async updateEvent(eventId: number, payload: UpdateEventRequest): Promise<EventResponse> {
    const response = await api.put<EventResponse>(`/events/${eventId}`, payload);
    return response.data;
  },

  /**
   * Delete an event owned by the authenticated user.
   */
  async deleteEvent(eventId: number): Promise<void> {
    await api.delete<void>(`/events/${eventId}`);
  },

  /**
   * Fetch events owned and created by the current user.
   */
  async getMyEvents(params?: PageParams): Promise<Page<EventResponse>> {
    const response = await api.get<Page<EventResponse>>('/users/me/events', { params });
    return response.data;
  },
};
