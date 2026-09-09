/**
 * Utility for conditionally joining class names.
 */
export function cn(...inputs: (string | undefined | null | false | 0)[]): string {
  return inputs.filter(Boolean).join(' ').trim();
}
