/**
 * Centralized token storage key and utility functions for managing JWT access tokens.
 */
export const ACCESS_TOKEN_KEY = 'teamup_access_token';

/**
 * Retrieve the current access token from localStorage.
 */
export function getAccessToken(): string | null {
  try {
    return localStorage.getItem(ACCESS_TOKEN_KEY);
  } catch {
    return null;
  }
}

/**
 * Store the access token in localStorage.
 */
export function setAccessToken(token: string): void {
  try {
    localStorage.setItem(ACCESS_TOKEN_KEY, token);
  } catch {
    // Gracefully handle storage errors (e.g. private browsing quota exceeded)
  }
}

/**
 * Remove the access token from localStorage.
 */
export function removeAccessToken(): void {
  try {
    localStorage.removeItem(ACCESS_TOKEN_KEY);
  } catch {
    // Gracefully handle storage errors
  }
}
