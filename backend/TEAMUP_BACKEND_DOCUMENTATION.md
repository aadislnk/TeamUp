# TeamUp Backend Overview

TeamUp is a Spring Boot backend for a student collaboration platform. The implementation supports account registration, email OTP verification, JWT login, profile management, skill discovery, event discovery and ownership-based event management, team creation and management, join requests, and notifications.

The primary goal of the backend is to let students discover technical events, create or join teams for those events, manage their public profile and skills, and coordinate team membership through join requests and notifications.

Target users are registered students or developers using the TeamUp platform. Unauthenticated guests can access public discovery APIs such as event listing, team listing, public profiles, public skills, and user search.

## Project Architecture

The project follows a layered Spring Boot architecture:

- **Controller layer**: Exposes REST endpoints, validates request DTOs with Jakarta Bean Validation, and formats responses.
- **Service layer**: Contains business logic, ownership checks, authentication-related workflows, OTP rules, team rules, join-request rules, and notification behavior.
- **Repository layer**: Uses Spring Data JPA repositories and specifications for persistence and querying.
- **Entity layer**: Defines database tables and relationships using JPA annotations.
- **DTO layer**: Separates request/response payloads from entities.
- **Mapper layer**: Converts entities to DTOs and request DTOs to entities.
- **Security layer**: Implements stateless JWT authentication with Spring Security.
- **Configuration layer**: Configures OpenAPI, Swagger, JPA auditing, OTP properties, mail properties, security, and database seeding.

Request flow:

1. HTTP request reaches a controller.
2. Spring validates request DTO annotations where `@Valid` is used.
3. Spring Security authenticates JWTs before protected endpoints.
4. Controller calls a service.
5. Service applies business rules and ownership checks.
6. Service uses repositories for persistence.
7. Mapper creates response DTOs.
8. Controller returns either direct DTO/page responses or `ApiResponse<T>` wrappers depending on module.

## Technology Stack

- **Language**: Java 21
- **Framework**: Spring Boot 4.1.0
- **Web**: Spring Web MVC
- **Persistence**: Spring Data JPA, Hibernate ORM
- **Database**: PostgreSQL
- **Security**: Spring Security, stateless JWT authentication
- **JWT library**: JJWT `0.12.7`
- **Validation**: Jakarta Bean Validation, Hibernate Validator URL constraint
- **Mail**: Spring Boot Mail, JavaMail
- **Documentation**: Springdoc OpenAPI / Swagger UI
- **Build tool**: Maven
- **Boilerplate reduction**: Lombok
- **Testing**: JUnit, Mockito, Spring Boot test starters
- **Runtime configuration**: `application.properties`, optional `application-local.properties`

## Database Design

All main entities extend `BaseEntity`, which provides:

- `id`
- `createdAt`
- `updatedAt`

JPA auditing is enabled through `JpaAuditingConfig`.

### User

Purpose: Represents a registered account.

Important fields:

- `fullName`
- `email`
- `password`
- `college`
- `role`
- `academicYear`
- `gender`
- `bio`
- `githubUrl`
- `linkedinUrl`
- `whatsappNumber`
- `emailVerified`
- `preferredRole`
- `profileImageUrl`

Relationships:

- One user has many email verification tokens.
- One user has many notifications.
- One user can lead many teams.
- One user can own many events.
- One user has many user-skill mappings.
- One user has many team memberships.
- One user has many join requests.

Responsibilities:

- Stores account identity and profile data.
- Acts as the owner for events.
- Acts as team leader for teams.
- Receives notifications and join-request state.

### EmailVerificationToken

Purpose: Stores email verification OTP state.

Important fields:

- `otp`
- `purpose`
- `status`
- `verifiedAt`
- `expiresAt`
- `resendCount`
- `failedAttempts`
- `lastSentAt`

Relationships:

- Many tokens belong to one user.

Responsibilities:

- Tracks active, verified, expired, and revoked OTPs.
- Enforces expiry, resend count, cooldown, and failed attempt limits.

### Skill

Purpose: Master skill catalog.

Important fields:

- `name`

Relationships:

- One skill can be referenced by many user-skill mappings.
- One skill can be referenced by many team-skill mappings.

Responsibilities:

- Provides searchable technical skill names.
- Seeded automatically if the skills table is empty.

### UserSkill

Purpose: Join table between users and skills.

Relationships:

- Many user-skill rows belong to one user.
- Many user-skill rows belong to one skill.

Constraints:

- Unique `(user_id, skill_id)`.

Responsibilities:

- Stores a user’s selected skills.

### Event

Purpose: Represents a technical event or competition.

Important fields:

- `title`
- `description`
- `organizer`
- `location`
- `minTeamSize`
- `maxTeamSize`
- `registrationOpen`
- `eventUrl`
- `registrationUrl`
- `bannerUrl`
- `mode`
- `status`
- `type`
- `registrationStart`
- `registrationEnd`
- `eventStart`
- `eventEnd`

Relationships:

- Many events belong to one owner user.
- One event can have many teams.

Responsibilities:

- Public event discovery.
- Ownership-based event creation/update/delete.
- Team creation context.

### Team

Purpose: Represents a team formed for an event.

Important fields:

- `name`
- `description`
- `maxMembers`
- `currentMembers`
- `recruitmentOpen`
- `whatsappGroupLink`
- `status`

Relationships:

- Many teams belong to one event.
- Many teams are led by one user.
- One team has many team members.
- One team has many join requests.
- One team has many required skill mappings.

Responsibilities:

- Stores team details for event participation.
- Tracks recruitment status and membership count.
- Enforces leader ownership for management operations.

### TeamMember

Purpose: Stores users who belong to teams.

Important fields:

- `joinedAt`

Relationships:

- Many team members belong to one team.
- Many team memberships belong to one user.

Constraints:

- Unique `(team_id, user_id)`.

Responsibilities:

- Tracks accepted team membership.

### TeamSkill

Purpose: Join table between teams and required skills.

Relationships:

- Many team-skill rows belong to one team.
- Many team-skill rows belong to one skill.

Constraints:

- Unique `(team_id, skill_id)`.

Responsibilities:

- Stores skills required by a team.

### JoinRequest

Purpose: Represents a user’s request to join a team.

Important fields:

- `message`
- `status`

Relationships:

- Many join requests belong to one user.
- Many join requests belong to one team.

Constraints:

- Unique `(user_id, team_id)`.

Responsibilities:

- Tracks pending, accepted, and rejected join requests.
- Drives notification and email behavior for team leaders and applicants.

### Notification

Purpose: In-app notification record.

Important fields:

- `title`
- `message`
- `type`
- `isRead`

Relationships:

- Many notifications belong to one user.

Responsibilities:

- Stores join-request related notifications and read/unread state.

## Authentication Module

### Registration

Endpoint: `POST /api/v1/auth/register`

Implemented behavior:

- Validates full name, email, password, confirm password, college, academic year, and gender.
- Confirms password and confirm password match.
- If an email exists and is already verified, registration fails with conflict.
- If an email exists but is not verified, the existing user is updated with the submitted registration data.
- New users are assigned role `USER`.
- Passwords are encoded with BCrypt before saving.
- Registration triggers email verification OTP sending.
- Response includes email, success message, and `verificationRequired=true`.

### Email Verification and OTP

Endpoints:

- `POST /api/v1/auth/send-otp`
- `POST /api/v1/auth/verify-otp`
- `POST /api/v1/auth/resend-otp`

Implemented behavior:

- OTP is generated using `SecureRandom`.
- OTP length is configured by `app.otp.length`; current configured length is `6`.
- OTP expiry is configured by `app.otp.expiry-minutes`; current configured expiry is `10` minutes.
- Resend cooldown is configured by `app.otp.resend-cooldown-seconds`; current configured cooldown is `60` seconds.
- Maximum resend attempts are configured by `app.otp.max-resend-attempts`; current configured value is `5`.
- Maximum verification attempts are configured by `app.otp.max-verification-attempts`; current configured value is `5`.
- Sending a new OTP revokes the previous active token.
- Verifying an expired token marks it `EXPIRED`.
- Verifying a wrong OTP increments failed attempts.
- Reaching max failed attempts revokes the token.
- Correct OTP marks token `VERIFIED`, sets `verifiedAt`, and sets `user.emailVerified=true`.
- Sending/resending OTP for an already verified user returns without sending a new OTP.
- Verifying an already verified user throws `AlreadyVerifiedException`.

### Login

Endpoint: `POST /api/v1/auth/login`

Implemented behavior:

- Looks up user by email.
- Verifies password using BCrypt.
- Blocks login when `emailVerified` is false.
- Generates a JWT when credentials are valid and email is verified.
- Returns token, token type, expiry, email, full name, and role.

### JWT Authentication

Implemented behavior:

- JWT subject is the user email.
- JWT includes issued-at and expiration timestamps.
- Token is signed with a Base64-decoded secret.
- Authentication filter reads the `Authorization` header.
- Header must start with `Bearer `.
- If token is valid, `SecurityContextHolder` is populated with `UsernamePasswordAuthenticationToken`.
- User details are loaded by email.

Not implemented:

- Logout is not implemented, although a logout message constant exists.
- Password reset/change is not implemented, although a password-changed message constant exists.
- JWT custom role/email claims are not implemented, although constants for claim names exist.

## User Module

### My Profile

Endpoint: `GET /api/v1/users/me`

Authentication required.

Returns:

- id
- fullName
- email
- bio
- preferredRole
- githubUrl
- linkedinUrl
- whatsappNumber
- profileImageUrl
- emailVerified
- createdAt

### Update Profile

Endpoint: `PUT /api/v1/users/me`

Authentication required.

Implemented behavior:

- Updates full name.
- Updates bio, GitHub URL, LinkedIn URL, WhatsApp number, and preferred role.
- Optional string fields keep existing values if the submitted value is null or blank.
- Updates avatar/profile image if avatar is present.

### Public Profile

Endpoint: `GET /api/v1/users/{userId}`

Public.

Returns:

- id
- fullName
- bio
- preferredRole
- githubUrl
- linkedinUrl
- profileImageUrl
- createdAt

Sensitive data not exposed:

- password
- email
- WhatsApp number
- role
- email verification status

### Profile Picture

Endpoints:

- `PUT /api/v1/users/me/profile-picture`
- `DELETE /api/v1/users/me/profile-picture`

Implemented behavior:

- Profile picture is represented by an `Avatar` enum value.
- Updating profile picture sets `profileImageUrl` to the selected avatar file name.
- Removing profile picture resets the user to the default avatar.

## Skills Module

### Master Skills

Endpoint: `GET /api/v1/skills`

Public.

Implemented behavior:

- Returns all skills sorted by name ascending.
- Skills are seeded at startup if the skills table is empty.

### User Skills

Endpoints:

- `GET /api/v1/users/me/skills`
- `POST /api/v1/users/me/skills`
- `DELETE /api/v1/users/me/skills/{skillId}`
- `GET /api/v1/users/{userId}/skills`

Implemented behavior:

- Authenticated users can list their own skills.
- Authenticated users can add multiple skills by ID.
- Duplicate submitted skill IDs are deduplicated in service logic.
- Already-added skills are ignored rather than duplicated.
- Invalid skill IDs fail with a not-found error.
- Users can remove a skill only if it exists on their profile.
- Public user skill retrieval is supported by user ID.

### User Search by Skills

Endpoint: `GET /api/v1/users/search?skillIds=...`

Public.

Implemented behavior:

- If one skill ID is provided, users with that skill are returned.
- If multiple skill IDs are provided, users with any of the valid skill IDs are returned.
- Invalid skill IDs are filtered out for multi-skill search.
- If no valid skill IDs remain, an empty page is returned.

## Events Module

### Public Event Listing

Endpoint: `GET /api/v1/events`

Public.

Implemented behavior:

- Returns paginated event responses.
- Results are sorted by `eventStart` ascending.

### Event Details

Endpoint: `GET /api/v1/events/{eventId}`

Public.

Implemented behavior:

- Returns a full event response.
- Throws not found if the event ID does not exist.

### Event Search and Filtering

Endpoint: `GET /api/v1/events/search`

Public.

Implemented behavior:

- If `keyword` is present and non-blank, searches title, description, organizer, and location case-insensitively.
- If `keyword` is absent or blank, filters by:
  - type
  - mode
  - status
  - registrationOpen
- Results are sorted by `eventStart` ascending.

### Upcoming Events

Endpoint: `GET /api/v1/events/upcoming`

Public.

Implemented behavior:

- Returns events where `eventStart >= now`.
- Results are sorted by `eventStart` ascending.

### Event Creation

Endpoint: `POST /api/v1/events`

Authentication required.

Implemented behavior:

- Validates dates, team capacity, status, and duplicate event title.
- Uses the authenticated user as event owner.
- Frontend does not send owner ID.
- Calculates `registrationOpen` from current time and registration window.

### Event Update

Endpoint: `PUT /api/v1/events/{eventId}`

Authentication required.

Implemented behavior:

- Loads the event.
- Validates that current user is event owner.
- Validates dates, team capacity, status, and duplicate title excluding the current event.
- Updates event fields through `EventMapper`.
- Non-owner update returns forbidden through `ForbiddenException`.

### Event Delete

Endpoint: `DELETE /api/v1/events/{eventId}`

Authentication required.

Implemented behavior:

- Loads the event.
- Validates that current user is event owner.
- Blocks deletion if event has associated teams.
- Deletes event if owned and no teams are attached.

### My Events

Endpoint: `GET /api/v1/users/me/events`

Authentication required.

Implemented behavior:

- Returns paginated events owned by the current authenticated user.
- Results are sorted by `eventStart` ascending.

### Event Response Owner Fields

Event responses expose:

- `ownerId`
- `ownerName`
- `ownerProfileImage`

Sensitive owner information is not exposed.

## Teams Module

### Public Team Listing

Endpoints:

- `GET /api/v1/teams`
- `GET /api/v1/teams/{teamId}`
- `GET /api/v1/teams/search`

Public.

Implemented behavior:

- Lists all teams.
- Retrieves team by ID.
- Searches teams with optional keyword, event ID, required skill ID, recruitment status, and team status.
- Team responses include required skills and member lists.

### Team Creation

Endpoint: `POST /api/v1/teams`

Authentication required.

Implemented behavior:

- Requires an existing event.
- Prevents duplicate team name for the same event.
- Current authenticated user becomes team leader.
- Prevents the same user from leading more than one team for the same event.
- New teams start with:
  - `currentMembers=1`
  - `status=OPEN`
  - submitted recruitment status

Important implementation detail:

- Team creation stores the leader and sets `currentMembers=1`, but the service does not create a `TeamMember` row for the leader.

### Team Update

Endpoint: `PUT /api/v1/teams/{teamId}`

Authentication required.

Implemented behavior:

- Only team leader can update.
- Prevents duplicate team name for the same event.
- Prevents lowering max members below current member count.
- Updates name, description, max members, recruitment status, and WhatsApp group link.

### Team Delete

Endpoint: `DELETE /api/v1/teams/{teamId}`

Authentication required.

Implemented behavior:

- Only team leader can delete.
- Team cannot be deleted when `currentMembers > 1`.

### Recruitment and WhatsApp Link

Endpoints:

- `PATCH /api/v1/teams/{teamId}/recruitment`
- `PATCH /api/v1/teams/{teamId}/whatsapp`

Authentication required.

Implemented behavior:

- Only team leader can update recruitment or WhatsApp group link.
- Opening recruitment requires team status `OPEN`.
- Opening recruitment requires team capacity to be available.

### Required Team Skills

Endpoints:

- `POST /api/v1/teams/{teamId}/skills/{skillId}`
- `DELETE /api/v1/teams/{teamId}/skills/{skillId}`

Authentication required.

Implemented behavior:

- Only team leader can add/remove required skills.
- Skill must exist.
- Duplicate team required skills are rejected.
- Removing a missing required skill returns not found.

### Implemented But Not API-Exposed Team Service Methods

The service layer implements these capabilities, but no controller endpoint currently exposes them:

- Get open teams.
- Get teams by status.
- Get teams led by the current user.
- Get required skills for a team.
- Get team members.
- Get leader dashboard with pending request count, accepted member count, and recruitment status.

## Join Request Module

### Apply to Team

Endpoint: `POST /api/v1/teams/{teamId}/apply`

Authentication required.

Implemented behavior:

- Loads the target team.
- Current user becomes applicant.
- Team leader cannot apply to their own team.
- Team recruitment must be open.
- Team must have capacity.
- Applicant must not already be a member of the team.
- Applicant must not already have a pending request for the team.
- Applicant must not already be a member of another team for the same event.
- Creates a `PENDING` join request.
- Creates an in-app notification for the team leader.
- Sends a join-request-received email to the team leader.

### Withdraw Request

Endpoint: `DELETE /api/v1/requests/{requestId}`

Authentication required.

Implemented behavior:

- Only the applicant can withdraw.
- Only pending requests can be withdrawn.
- Creates a notification for the team leader.
- Deletes the join request.

### Accept Request

Endpoint: `PATCH /api/v1/requests/{requestId}/accept`

Authentication required.

Implemented behavior:

- Only team leader can accept.
- Request must be pending.
- Recruitment must be open.
- Team must have capacity.
- Applicant must not already be a member.
- Applicant must not already be in another team for the event.
- Creates a `TeamMember`.
- Increments `currentMembers`.
- Closes recruitment if team reaches max members.
- Marks request `ACCEPTED`.
- Creates notification for applicant.
- Sends join-request-accepted email to applicant.

### Reject Request

Endpoint: `PATCH /api/v1/requests/{requestId}/reject`

Authentication required.

Implemented behavior:

- Only team leader can reject.
- Request must be pending.
- Marks request `REJECTED`.
- Creates notification for applicant.
- Sends join-request-rejected email to applicant.

### Incoming Requests

Endpoint: `GET /api/v1/teams/{teamId}/requests`

Authentication required.

Implemented behavior:

- Only team leader can view incoming requests.
- Returns pending requests for the team.

### Outgoing Requests

Endpoint: `GET /api/v1/users/me/requests`

Authentication required.

Implemented behavior:

- Returns all join requests created by current user.

## Notifications Module

### My Notifications

Endpoint: `GET /api/v1/users/me/notifications`

Authentication required.

Implemented behavior:

- Returns notifications for current user ordered by created date descending.

### Unread Count

Endpoint: `GET /api/v1/users/me/notifications/unread-count`

Authentication required.

Implemented behavior:

- Returns count of unread notifications for current user.

### Mark One Notification as Read

Endpoint: `PATCH /api/v1/notifications/{id}/read`

Authentication required.

Implemented behavior:

- Loads notification by ID.
- Validates notification belongs to current user.
- If already read, returns without saving.
- Otherwise sets `isRead=true`.

Important implementation detail:

- Ownership failure throws `java.nio.file.AccessDeniedException`, not the custom `ForbiddenException`. The global exception handler does not have a dedicated handler for this exception type, so this may be handled by the generic exception handler.

### Mark All Notifications as Read

Endpoint: `PATCH /api/v1/notifications/read-all`

Authentication required.

Implemented behavior:

- Finds unread notifications for current user.
- If none exist, returns.
- Marks all unread notifications as read and saves them.

### Supported Notification Types

Enum values:

- `JOIN_REQUEST_RECEIVED`
- `JOIN_REQUEST_ACCEPTED`
- `JOIN_REQUEST_REJECTED`
- `JOIN_REQUEST_WITHDRAWN`
- `TEAM_INVITATION`

Implemented creation flows:

- Join request received.
- Join request withdrawn.
- Join request accepted.
- Join request rejected.

Partially implemented:

- Team invitation email template and notification type exist, but no controller/service workflow exposes team invitations.

## Validation Rules

### Authentication

Registration:

- `fullName`: required, 3 to 50 characters.
- `email`: required, valid email, max 255 characters.
- `password`: required, 8 to 100 characters.
- `confirmPassword`: required, 8 to 100 characters.
- `college`: required, max 150 characters.
- `academicYear`: required.
- `gender`: required.
- Password and confirm password must match.

Login:

- `email`: required, valid email, max 255 characters.
- `password`: required, 8 to 100 characters.

OTP:

- Email is required and must be valid.
- OTP is required and must match six digits.

### Profile

- `fullName`: required, 2 to 100 characters.
- `githubUrl`: must be a valid URL when provided.
- `linkedinUrl`: must be a valid URL when provided.
- `whatsappNumber`: must match `^\+?[1-9]\d{7,14}$`.
- `avatar`: required for profile-picture update.

### Skills

- Add-skills request requires a non-empty list.
- Skill IDs in add-skills request cannot be null.
- Service rejects null or non-positive skill IDs.

### Events

Create/update event:

- `title`: required, max 255 characters.
- `description`: max 5000 characters.
- `organizer`: required, max 255 characters.
- `location`: max 255 characters.
- `mode`: required.
- `status`: required.
- `type`: required.
- `minTeamSize`: required, minimum 1.
- `maxTeamSize`: required, minimum 1.
- `registrationStart`: required.
- `registrationEnd`: required.
- `eventStart`: required.
- `eventEnd`: required.

Business validation:

- Registration start must be before registration end.
- Event start must be before event end.
- Registration end cannot be after event start.
- Minimum team size cannot be greater than maximum team size.
- Completed events cannot have registration open.
- Event title must be unique case-insensitively.
- Event deletion is blocked if teams exist for the event.

### Teams

Create/update team:

- `name`: required, 3 to 100 characters.
- `description`: max 2000 characters.
- `maxMembers`: required, min 2, max 50.
- `recruitmentOpen`: required.
- `whatsappGroupLink`: must match WhatsApp invite URL pattern when provided.
- `eventId`: required for team creation.

Business validation:

- Team name must be unique within an event.
- Current user cannot lead more than one team for the same event.
- Only team leader can update, delete, manage recruitment, manage WhatsApp link, and manage required skills.
- Maximum members cannot be lower than current members.
- Team cannot be deleted if `currentMembers > 1`.
- Recruitment can only be opened when team status is `OPEN` and capacity is available.

### Join Requests

- Application message max length is 500.
- Applicant cannot be team leader.
- Team recruitment must be open.
- Team must have remaining capacity.
- Applicant cannot already be a team member.
- Applicant cannot already have a pending request for the team.
- Applicant cannot already be a member of another team for the same event.
- Only applicant can withdraw request.
- Only pending requests can be accepted/rejected/withdrawn.
- Only team leader can accept/reject/view incoming requests.

## Security

### Public Routes

Implemented public route rules:

- `/swagger-ui/**`
- `/v3/api-docs/**`
- `/api/v1/auth/**`
- `GET /api/v1/skills`
- `GET /api/v1/users/{userId}`
- `GET /api/v1/users/{userId}/skills`
- `GET /api/v1/users/search`
- `GET /api/v1/events`
- `GET /api/v1/events/**`
- `GET /api/v1/teams`
- `GET /api/v1/teams/**`

### Protected Routes

Implemented protected route rules:

- `/api/v1/users/me`
- `/api/v1/users/me/**`
- `POST /api/v1/teams`
- `PUT /api/v1/teams/**`
- `PATCH /api/v1/teams/**`
- `DELETE /api/v1/teams/**`
- Any other route not explicitly public.

Because event mutation endpoints are not public GET routes and no specific permit rule applies to them, `POST /api/v1/events`, `PUT /api/v1/events/{eventId}`, and `DELETE /api/v1/events/{eventId}` require authentication through `anyRequest().authenticated()`.

### Ownership Authorization

Implemented ownership checks:

- Event update/delete: only event owner.
- Team update/delete/recruitment/WhatsApp/required skills: only team leader.
- Join request withdrawal: only applicant.
- Join request accept/reject/incoming requests: only team leader.
- Notification mark-as-read: only notification owner.

Not implemented:

- Role-based admin authorization is not implemented.
- Organizer role is not implemented.
- Fine-grained permissions beyond ownership are not implemented.

## Exception Handling

`GlobalExceptionHandler` handles:

- `BadRequestException` → 400
- `ResourceNotFoundException` → 404
- `UserNotFoundException` → 404
- `ConflictException` → 409
- `UnauthorizedException` → 401
- `ForbiddenException` → 403
- `InternalServerException` → 500
- `OtpExpiredException` → 400
- `MethodArgumentNotValidException` → 400 with validation errors
- `ConstraintViolationException` → 400 with validation errors
- `HttpMessageNotReadableException` → 400
- generic `Exception` → 500

Authentication entry point:

- JWT authentication failures return 401 with JSON error response.

Important implementation detail:

- `ErrorResponse` contains a `status` field, but `GlobalExceptionHandler.buildErrorResponse` does not set it. The JWT authentication entry point does set status.
- Several OTP exceptions such as invalid OTP, OTP not found, resend cooldown, too many OTP requests, too many verification attempts, and already verified are not individually mapped in `GlobalExceptionHandler`; they currently fall through to the generic exception handler unless handled elsewhere by Spring.

## API Summary

| Method | Endpoint | Authentication Required | Purpose | Controller |
|---|---|---:|---|---|
| POST | `/api/v1/auth/register` | No | Register a user and send verification OTP | `AuthenticationController` |
| POST | `/api/v1/auth/login` | No | Login and receive JWT | `AuthenticationController` |
| POST | `/api/v1/auth/send-otp` | No | Send verification OTP | `EmailVerificationController` |
| POST | `/api/v1/auth/verify-otp` | No | Verify email with OTP | `EmailVerificationController` |
| POST | `/api/v1/auth/resend-otp` | No | Resend verification OTP | `EmailVerificationController` |
| GET | `/api/v1/users/me` | Yes | Get current user profile | `UserProfileController` |
| PUT | `/api/v1/users/me` | Yes | Update current user profile | `UserProfileController` |
| GET | `/api/v1/users/{userId}` | No | Get public profile | `UserProfileController` |
| PUT | `/api/v1/users/me/profile-picture` | Yes | Update avatar/profile picture | `UserProfileController` |
| DELETE | `/api/v1/users/me/profile-picture` | Yes | Reset profile picture to default | `UserProfileController` |
| GET | `/api/v1/skills` | No | List master skills | `SkillController` |
| GET | `/api/v1/users/me/skills` | Yes | List current user skills | `SkillController` |
| POST | `/api/v1/users/me/skills` | Yes | Add skills to current user | `SkillController` |
| DELETE | `/api/v1/users/me/skills/{skillId}` | Yes | Remove current user skill | `SkillController` |
| GET | `/api/v1/users/{userId}/skills` | No | List public user skills | `SkillController` |
| GET | `/api/v1/users/search` | No | Search users by skill IDs | `SkillController` |
| GET | `/api/v1/events` | No | List events | `EventController` |
| GET | `/api/v1/events/{eventId}` | No | Get event details | `EventController` |
| GET | `/api/v1/events/search` | No | Search/filter events | `EventController` |
| GET | `/api/v1/events/upcoming` | No | List upcoming events | `EventController` |
| POST | `/api/v1/events` | Yes | Create owned event | `EventController` |
| PUT | `/api/v1/events/{eventId}` | Yes | Update owned event | `EventController` |
| DELETE | `/api/v1/events/{eventId}` | Yes | Delete owned event | `EventController` |
| GET | `/api/v1/users/me/events` | Yes | List current user’s owned events | `EventController` |
| GET | `/api/v1/teams` | No | List teams | `TeamController` |
| GET | `/api/v1/teams/{teamId}` | No | Get team details | `TeamController` |
| GET | `/api/v1/teams/search` | No | Search/filter teams | `TeamController` |
| POST | `/api/v1/teams` | Yes | Create team | `TeamController` |
| PUT | `/api/v1/teams/{teamId}` | Yes | Update own led team | `TeamController` |
| DELETE | `/api/v1/teams/{teamId}` | Yes | Delete own led team | `TeamController` |
| PATCH | `/api/v1/teams/{teamId}/recruitment` | Yes | Update recruitment status | `TeamController` |
| PATCH | `/api/v1/teams/{teamId}/whatsapp` | Yes | Update WhatsApp group link | `TeamController` |
| POST | `/api/v1/teams/{teamId}/skills/{skillId}` | Yes | Add required team skill | `TeamController` |
| DELETE | `/api/v1/teams/{teamId}/skills/{skillId}` | Yes | Remove required team skill | `TeamController` |
| POST | `/api/v1/teams/{teamId}/apply` | Yes | Apply to join team | `JoinRequestController` |
| DELETE | `/api/v1/requests/{requestId}` | Yes | Withdraw join request | `JoinRequestController` |
| PATCH | `/api/v1/requests/{requestId}/accept` | Yes | Accept join request | `JoinRequestController` |
| PATCH | `/api/v1/requests/{requestId}/reject` | Yes | Reject join request | `JoinRequestController` |
| GET | `/api/v1/teams/{teamId}/requests` | Yes | List incoming pending requests | `JoinRequestController` |
| GET | `/api/v1/users/me/requests` | Yes | List current user outgoing requests | `JoinRequestController` |
| GET | `/api/v1/users/me/notifications` | Yes | List current user notifications | `NotificationController` |
| GET | `/api/v1/users/me/notifications/unread-count` | Yes | Get unread notification count | `NotificationController` |
| PATCH | `/api/v1/notifications/{id}/read` | Yes | Mark one notification as read | `NotificationController` |
| PATCH | `/api/v1/notifications/read-all` | Yes | Mark all notifications as read | `NotificationController` |

## Business Rules

- Users must verify email before login.
- Registration always assigns role `USER`.
- A verified email cannot be registered again.
- An unverified existing account can be updated by registering again with the same email.
- OTP resend has cooldown and maximum resend count.
- OTP verification has expiry and failed-attempt limits.
- Public profiles do not expose email, password, WhatsApp number, role, or verification status.
- Users can manage only their own profile.
- Users can manage only their own skills.
- Users can browse public skills.
- Users can search other users by skills.
- Anyone can browse events, event details, event search, and upcoming events.
- Authenticated users can create events.
- Event creator becomes event owner.
- Only event owner can update or delete an event.
- Events cannot be deleted if teams are associated.
- Team creator becomes team leader.
- Users cannot lead more than one team for the same event.
- Team names must be unique within an event.
- Only team leader can update/delete a team.
- Team cannot be deleted if it has more than one current member.
- Only team leader can manage recruitment, WhatsApp link, and required skills.
- Required skills cannot be duplicated on a team.
- Applicants cannot apply to their own team.
- Applicants cannot apply if recruitment is closed.
- Applicants cannot apply if team is full.
- Applicants cannot apply if already a team member.
- Applicants cannot create duplicate pending join requests.
- Applicants cannot join more than one team for the same event.
- Only applicant can withdraw a join request.
- Only pending requests can be accepted, rejected, or withdrawn.
- Only team leader can accept/reject requests or view incoming pending requests.
- Accepting a request creates team membership and increments current member count.
- Accepting a request closes recruitment if max members is reached.
- Join request actions create notifications.
- Join request received/accepted/rejected actions send emails.
- Users can only mark their own notifications as read.

## Project Workflows

### Registration and Login

1. User registers with profile basics, email, password, academic year, and gender.
2. Backend validates request and password confirmation.
3. Backend saves user with encoded password, role `USER`, default avatar, and `emailVerified=false`.
4. Backend sends email verification OTP.
5. User submits OTP.
6. Backend verifies OTP and marks user email verified.
7. User logs in.
8. Backend returns JWT.

### Profile Setup

1. Authenticated user calls `GET /api/v1/users/me`.
2. User updates full name, bio, preferred role, URLs, WhatsApp number, and avatar.
3. Backend validates and stores profile changes.
4. Other users can view public profile by user ID.

### Skill Setup and Discovery

1. User lists master skills.
2. User adds skill IDs to their profile.
3. User can remove skills.
4. Other users can view a user’s public skills.
5. Users can search profiles by one or more skill IDs.

### Event Ownership Flow

1. Guest browses events.
2. Authenticated user creates an event.
3. Backend assigns current user as event owner.
4. Owner can list their events with `/api/v1/users/me/events`.
5. Owner can update the event.
6. Owner can delete the event if it has no associated teams.
7. Non-owner receives forbidden response for update/delete.

### Team and Join Request Flow

1. User browses events.
2. Authenticated user creates a team for an event.
3. Backend assigns current user as team leader.
4. Other authenticated users apply to join the team.
5. Backend validates recruitment, capacity, duplicate request, and event-level team membership.
6. Team leader receives notification and email.
7. Team leader accepts or rejects request.
8. Applicant receives notification and email.
9. On acceptance, applicant becomes a team member and team count increases.

### Notification Flow

1. Join request action creates notifications.
2. User lists notifications.
3. User checks unread count.
4. User marks one or all notifications as read.

## Current Features

| Feature | Status |
|---|---|
| Registration | Implemented |
| Email verification | Implemented |
| OTP generation | Implemented |
| OTP resend cooldown | Implemented |
| OTP resend max attempts | Implemented |
| OTP failed verification limit | Implemented |
| Login | Implemented |
| JWT authentication | Implemented |
| Logout | Not implemented |
| Password reset/change | Not implemented |
| Profile retrieval | Implemented |
| Profile update | Implemented |
| Avatar selection | Implemented |
| Public profile | Implemented |
| Master skills | Implemented |
| Skill seeding | Implemented |
| User skill management | Implemented |
| User search by skills | Implemented |
| Public event listing | Implemented |
| Event search/filtering | Implemented |
| Upcoming events | Implemented |
| Event creation | Implemented |
| Event ownership update/delete | Implemented |
| My events | Implemented |
| Public team listing | Implemented |
| Team search/filtering | Implemented |
| Team creation | Implemented |
| Team leader ownership checks | Implemented |
| Team required skills | Implemented |
| Join requests | Implemented |
| Join request emails | Implemented |
| Notifications | Implemented |
| Team invitations | Partially implemented: email template/type exists, no API workflow |
| Swagger/OpenAPI | Implemented |
| Global exception handling | Implemented |
| Tests | Partially implemented: context load test and event ownership unit tests |

## Known Limitations

- Logout is not implemented.
- Password reset/change is not implemented.
- Team invitation workflow is not exposed through a controller, although template, subject, and notification type exist.
- Several team service methods are implemented but not exposed by controllers: open teams, teams by status, my teams, required skills, team members, and leader dashboard.
- `OtpService.cleanupExpiredTokens()` is implemented but no scheduler or endpoint invokes it.
- `DateTimeUtil`, `PaginationUtil`, `ResponseUtil`, `StringUtil`, and `ValidationUtil` are empty placeholder utility classes.
- `BaseMapper` exists but is not used by concrete mappers.
- `SecurityConstants.PUBLIC_URLS`, `ROLE_CLAIM`, and `EMAIL_CLAIM` exist but are not used by the JWT/security implementation.
- JWT contains subject, issued-at, and expiration, but no custom role/email claims.
- Global exception responses do not set the `status` field in `ErrorResponse`.
- Some OTP exceptions are not specifically handled by `GlobalExceptionHandler`.
- Notification ownership failure uses `java.nio.file.AccessDeniedException`, which is not specifically handled by the global exception handler.
- Team creation increments logical membership count by setting `currentMembers=1`, but does not create a `TeamMember` row for the leader.
- Event URL, registration URL, and banner URL fields do not have validation annotations in event request DTOs.
- The application uses `spring.jpa.hibernate.ddl-auto=update`; no Flyway or Liquibase migration system is implemented.

## Future Scope Visible From Current Architecture

These are not implemented features, but the codebase contains structures suggesting possible future work:

- Team invitations: notification type, email subject, template builder method, and email service method exist.
- Scheduled OTP cleanup: `cleanupExpiredTokens()` exists in `OtpService`.
- Exposing additional team service methods through controllers: open teams, teams by status, my teams, required skills, members, and leader dashboard.
- Shared mapper abstraction: `BaseMapper` exists but is not currently used.
- Shared utility methods: utility classes exist but are empty.
- JWT custom claims: claim constants exist but tokens do not currently include them.

## Project Highlights

- Layered architecture with clear separation between controllers, services, repositories, entities, DTOs, and mappers.
- DTO pattern prevents direct entity exposure.
- Global exception handling provides consistent error response structure for many business and validation errors.
- JWT authentication is stateless and integrated through a `OncePerRequestFilter`.
- Ownership-based authorization is enforced in the service layer for events, teams, join requests, and notifications.
- Validation is implemented with Jakarta Bean Validation annotations and service-level business validation.
- Event and team search use Spring Data JPA `Specification` patterns.
- Database design includes explicit join entities for user skills, team skills, team members, and join requests.
- Swagger/OpenAPI configuration is present with bearer authentication schemes.
- Email templates are separated into resource files and populated through a template builder.
- Seed data initializes a broad master skill catalog.
