/**
 * Standard API Response wrapper returned by Spring Boot backend.
 */
export interface ApiResponse<T = void> {
  success: boolean;
  message: string;
  data: T;
  timestamp: string;
}

/**
 * Field-level validation error returned on 400 Bad Request.
 */
export interface ValidationError {
  field?: string | null;
  message: string;
}

/**
 * Standard Error Response payload returned on exceptions.
 */
export interface ErrorResponse {
  success: boolean;
  status: number;
  message: string;
  errors?: ValidationError[] | null;
  timestamp: string;
  path?: string | null;
}

/**
 * Sort metadata within Spring Data Page serialization.
 */
export interface PageSort {
  empty: boolean;
  sorted: boolean;
  unsorted: boolean;
}

/**
 * Pageable metadata within Spring Data Page serialization.
 */
export interface PageableObject {
  pageNumber: number;
  pageSize: number;
  sort: PageSort;
  offset: number;
  paged: boolean;
  unpaged: boolean;
}

/**
 * Spring Data Page JSON structure returned by backend controllers (org.springframework.data.domain.Page).
 */
export interface SpringPage<T> {
  content: T[];
  pageable: PageableObject;
  last: boolean;
  totalPages: number;
  totalElements: number;
  first: boolean;
  size: number;
  number: number;
  sort: PageSort;
  numberOfElements: number;
  empty: boolean;
}

/**
 * Alias for SpringPage for convenience.
 */
export type Page<T> = SpringPage<T>;

/**
 * Pagination and sorting query parameters sent to the backend.
 */
export interface PageParams {
  page?: number;
  size?: number;
  sort?: string | string[];
}
