import { createContext } from 'react';
import type { AuthContextValue } from '../../types';

/**
 * React Context for application authentication state.
 */
export const AuthContext = createContext<AuthContextValue | null>(null);
