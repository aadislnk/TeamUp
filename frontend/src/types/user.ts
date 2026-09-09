/**
 * Academic year classification for student profiles.
 */
export type AcademicYear =
  | 'FIRST_YEAR'
  | 'SECOND_YEAR'
  | 'THIRD_YEAR'
  | 'FOURTH_YEAR'
  | 'GRADUATED';

/**
 * Gender options.
 */
export type Gender = 'MALE' | 'FEMALE' | 'OTHER';

/**
 * Technical / development specialization roles.
 */
export type PreferredRole =
  | 'BACKEND_DEVELOPER'
  | 'FRONTEND_DEVELOPER'
  | 'FULL_STACK_DEVELOPER'
  | 'MOBILE_APP_DEVELOPER'
  | 'ANDROID_DEVELOPER'
  | 'IOS_DEVELOPER'
  | 'AI_ML_ENGINEER'
  | 'DATA_SCIENTIST'
  | 'DATA_ANALYST'
  | 'DEVOPS_ENGINEER'
  | 'CLOUD_ENGINEER'
  | 'UI_UX_DESIGNER'
  | 'GRAPHIC_DESIGNER'
  | 'PRODUCT_MANAGER'
  | 'PROJECT_MANAGER'
  | 'QA_TEST_ENGINEER'
  | 'CYBERSECURITY_ENGINEER'
  | 'BLOCKCHAIN_DEVELOPER'
  | 'GAME_DEVELOPER'
  | 'EMBEDDED_SYSTEMS_ENGINEER'
  | 'IOT_DEVELOPER'
  | 'AR_VR_DEVELOPER'
  | 'OPEN_SOURCE_CONTRIBUTOR'
  | 'RESEARCHER'
  | 'OTHER';

/**
 * Predefined avatar choices.
 */
export type Avatar =
  | 'DEFAULT'
  | 'AVATAR_01'
  | 'AVATAR_02'
  | 'AVATAR_03'
  | 'AVATAR_04'
  | 'AVATAR_05'
  | 'AVATAR_06'
  | 'AVATAR_07'
  | 'AVATAR_08'
  | 'AVATAR_09'
  | 'AVATAR_10';

/**
 * Master catalog skill response.
 */
export interface SkillResponse {
  id: number;
  name: string;
}

/**
 * User skill association response.
 */
export interface UserSkillResponse {
  id: number;
  skill: SkillResponse;
}

/**
 * Request payload to associate skills with current authenticated user.
 */
export interface AddSkillsRequest {
  skillIds: number[];
}

/**
 * Request payload to update profile details for current user.
 */
export interface UpdateProfileRequest {
  fullName: string;
  bio?: string | null;
  preferredRole?: PreferredRole | null;
  githubUrl?: string | null;
  linkedinUrl?: string | null;
  whatsappNumber?: string | null;
  avatar?: Avatar | null;
}

/**
 * Request payload to update user profile picture/avatar.
 */
export interface UpdateProfilePictureRequest {
  avatar: Avatar;
}

/**
 * Complete profile response for the authenticated user (includes private contact fields).
 */
export interface MyProfileResponse {
  id: number;
  fullName: string;
  email: string;
  bio: string | null;
  preferredRole: PreferredRole | null;
  githubUrl: string | null;
  linkedinUrl: string | null;
  whatsappNumber: string | null;
  profileImageUrl: string | null;
  emailVerified: boolean;
  createdAt: string;
}

/**
 * Public profile response visible to other users (omits private contact fields).
 */
export interface PublicProfileResponse {
  id: number;
  fullName: string;
  bio: string | null;
  preferredRole: PreferredRole | null;
  githubUrl: string | null;
  linkedinUrl: string | null;
  profileImageUrl: string | null;
  createdAt: string;
}

/**
 * Summary user search result item when filtering by skills.
 */
export interface UserSearchResponse {
  id: number;
  fullName: string;
  college: string;
  profileImageUrl: string | null;
  bio: string | null;
}
