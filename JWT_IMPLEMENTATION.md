# JWT Authentication Implementation

## Overview
JWT (JSON Web Token) authentication has been successfully implemented in your Spring Boot project. All REST API endpoints are now protected with JWT authentication.

## What Was Added

### 1. Dependencies (build.gradle)
- `spring-boot-starter-security` - Spring Security framework
- `io.jsonwebtoken:jjwt-api:0.12.3` - JWT API
- `io.jsonwebtoken:jjwt-impl:0.12.3` - JWT Implementation
- `io.jsonwebtoken:jjwt-jackson:0.12.3` - JWT Jackson support

### 2. Security Components

#### JwtUtil.java
- Utility class for JWT token generation and validation
- Extracts claims from tokens
- Validates token expiration

#### JwtAuthenticationFilter.java
- Filter that intercepts requests and validates JWT tokens
- Extracts token from `Authorization: Bearer <token>` header
- Sets authentication context for valid tokens

#### CustomUserDetailsService.java
- Loads user details from database for Spring Security
- Implements UserDetailsService interface

#### SecurityConfig.java
- Configures Spring Security filter chain
- Sets up JWT authentication filter
- Configures public vs protected endpoints
- MVC pages (Thymeleaf) remain accessible without JWT
- All `/api/**` endpoints require JWT authentication (except `/api/auth/**`)

#### PlainTextPasswordEncoder.java
- Custom password encoder for backward compatibility
- Supports both plain text (existing users) and BCrypt (new users)
- New passwords are automatically encoded with BCrypt

### 3. Authentication Controller

#### AuthRestController.java
Provides three endpoints:

**POST /api/auth/login**
- Authenticates user with email and password
- Returns JWT token and user information
- Request body: `{"email": "user@example.com", "password": "password123"}`
- Response: `{"token": "...", "type": "Bearer", "user": {...}}`

**POST /api/auth/register**
- Registers a new user
- Returns JWT token and user information
- Request body: `{"email": "...", "password": "...", "firstName": "...", "lastName": "...", "phoneNumber": "...", "type": "CLIENT|GUIDE"}`
- Response: `{"token": "...", "type": "Bearer", "user": {...}}`

**GET /api/auth/validate**
- Validates an existing JWT token
- Header: `Authorization: Bearer <token>`
- Response: `{"valid": true/false, "user": {...}}`

## Configuration

### application.properties
Added JWT configuration:
```properties
jwt.secret=MySecretKeyForJWTTokenGenerationThatIsAtLeast256BitsLongForHS256AlgorithmToWorkProperly
jwt.expiration=86400000  # 24 hours in milliseconds
```

## How It Works

1. **Registration/Login**: User calls `/api/auth/register` or `/api/auth/login` to get a JWT token
2. **API Requests**: Client includes token in header: `Authorization: Bearer <token>`
3. **Token Validation**: JwtAuthenticationFilter validates token on each request
4. **Access Control**: Spring Security checks if user is authenticated before allowing access to protected endpoints

## Protected Endpoints

All REST API endpoints under `/api/**` (except `/api/auth/**`) now require JWT authentication:
- `/api/user/**`
- `/api/place/**`
- `/api/tour/**`
- `/api/guide/**`
- `/api/reservation/**`
- `/api/review/**`
- `/api/favorite/**`
- `/api/admin/**`

## Public Endpoints

- `/api/auth/**` - Authentication endpoints
- MVC pages (Thymeleaf) - `/`, `/login`, `/signup`, etc. (session-based auth still works)

## Testing with Postman

### 1. Register a new user:
```
POST http://localhost:8082/api/auth/register
Content-Type: application/json

{
  "email": "test@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "+1234567890",
  "type": "CLIENT"
}
```

### 2. Login:
```
POST http://localhost:8082/api/auth/login
Content-Type: application/json

{
  "email": "test@example.com",
  "password": "password123"
}
```

### 3. Use token in API requests:
```
GET http://localhost:8082/api/place/all
Authorization: Bearer <your-jwt-token>
```

## Notes

- Existing users with plain text passwords will still work (backward compatibility)
- New passwords are automatically encoded with BCrypt
- JWT tokens expire after 24 hours (configurable in application.properties)
- MVC pages continue to use session-based authentication
- REST APIs exclusively use JWT authentication

## Requirements Status

✅ **MVC Architecture**: Confirmed - Models, Views (Thymeleaf), Controllers, Services, Repositories
✅ **JWT Authentication**: Implemented - Full JWT support with token generation, validation, and security filter
✅ **REST APIs**: Confirmed - Multiple REST controllers with proper HTTP methods and responses

