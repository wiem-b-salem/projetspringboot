# COMPREHENSIVE TECHNICAL GUIDE - TUNIWAY SPRING BOOT PROJECT

**Project Name:** Projet Spring - Tour Booking Platform  
**Framework:** Spring Boot 4.0.0  
**Java Version:** 17  
**Database:** MySQL  
**Port:** 8082  

---

## TABLE OF CONTENTS

1. [Project Overview](#project-overview)
2. [Architecture Overview](#architecture-overview)
3. [JWT Security Implementation](#jwt-security-implementation)
4. [MVC Architecture](#mvc-architecture)
5. [REST API Architecture](#rest-api-architecture)
6. [Database Models & Relationships](#database-models--relationships)
7. [Core Components](#core-components)
8. [Authentication Flow](#authentication-flow)
9. [API Endpoints](#api-endpoints)
10. [Security Features](#security-features)

---

## PROJECT OVERVIEW

### What This Project Does

TuniWay is a **Tour Booking Platform** that allows:
- Users to register, login, and manage their accounts
- Browse tourist places and tour packages
- Reserve tours created by tour guides
- Leave reviews and ratings for tours
- Save favorite places
- Admins to manage the entire system via a dashboard and REST APIs

The application supports **two main user types**:
1. **Regular Users (Clients)** - Can browse and book tours, leave reviews
2. **Guides** - Are specialized users who can create and manage tours
3. **Admin** - System administrator with full control

---

## ARCHITECTURE OVERVIEW

### High-Level Architecture Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                        CLIENT LAYER                          │
│  (Thymeleaf HTML Templates + HTTP Client Requests)          │
└────────────────────────────┬────────────────────────────────┘
                             │
                    ┌────────▼────────┐
                    │  SECURITY LAYER │
                    │   JWT Filter    │
                    └────────┬────────┘
                             │
        ┌────────────────────┼────────────────────┐
        │                    │                    │
   ┌────▼─────┐    ┌────────▼────────┐    ┌─────▼──────┐
   │ MVC Ctrl  │    │ REST Controller  │    │JWT Utility  │
   └────┬─────┘    └────────┬────────┘    └─────┬──────┘
        │                   │                    │
        └───────────────────┼────────────────────┘
                            │
                    ┌───────▼────────┐
                    │ SERVICE LAYER  │
                    │ (Business Logic)│
                    └───────┬────────┘
                            │
                    ┌───────▼────────┐
                    │   REPOSITORY   │
                    │  (Data Access) │
                    └───────┬────────┘
                            │
                    ┌───────▼────────┐
                    │    DATABASE    │
                    │     (MySQL)    │
                    └────────────────┘
```

### Layered Architecture Pattern

**1. Controller Layer**
   - HTTP request handlers
   - Two types: MVC Controllers (Thymeleaf) and REST Controllers (JSON)
   - Validates authentication headers
   - Routes requests to services

**2. Service Layer**
   - Business logic implementation
   - Service interfaces (contracts)
   - Service implementations with @Service annotation
   - Transaction management

**3. Repository Layer**
   - Data access abstraction using Spring Data JPA
   - Database query methods
   - Extends JpaRepository for CRUD operations

**4. Model Layer**
   - JPA Entity classes (Domain objects)
   - Database table mappings
   - Entity relationships (One-to-Many, Many-to-One)

**5. Security Layer**
   - JWT token generation and validation
   - Custom authentication filter
   - Password encoding
   - User details service

---

## JWT SECURITY IMPLEMENTATION

### What is JWT (JSON Web Token)?

JWT is a **stateless** authentication mechanism where:
- User logs in → Server generates a signed token
- Client stores the token and sends it with every request
- Server validates the token without storing session data
- Token contains encoded claims (username, user type, expiration)

### JWT Components in This Project

#### 1. **JwtUtil.java** - Token Generation & Validation

**File Location:** `src/main/java/org/tp1ex2/projetspring/security/JwtUtil.java`

**Key Methods:**

```java
// 1. Generate Token
public String generateToken(String username, String userType)
- Creates JWT token with username and userType (ADMIN or regular user)
- Signs token using HMAC-SHA256 with secret key
- Sets expiration to 24 hours (86400000 ms)

// 2. Extract Information
public String extractUsername(String token)
- Extracts username (subject) from token claims

public String extractUserType(String token)
- Extracts user type (ADMIN or regular) from custom claims

public Date extractExpiration(String token)
- Gets token expiration date

// 3. Validate Token
public Boolean validateToken(String token, String username)
- Checks if token username matches provided username
- Checks if token is not expired

public Boolean isAdminToken(String token)
- Checks if token's user type is "ADMIN"

// 4. Internal Methods
private SecretKey getSigningKey()
- Converts JWT secret string to cryptographic key using HMAC-SHA256

private Claims extractAllClaims(String token)
- Parses and verifies token signature
- Extracts all claims if signature is valid
```

**JWT Token Structure:**

```
Header.Payload.Signature

Header: {
  "alg": "HS256",
  "typ": "JWT"
}

Payload: {
  "sub": "admin",           // username
  "type": "ADMIN",          // user type
  "iat": 1704369234,        // issued at
  "exp": 1704455634         // expiration time
}

Signature: HMACSHA256(Header + Payload, secret_key)
```

**Configuration (application.properties):**

```properties
jwt.secret=MySecretKeyForJWTTokenGenerationThatIsAtLeast256BitsLongForHS256AlgorithmToWorkProperly
jwt.expiration=86400000  # 24 hours in milliseconds
```

#### 2. **JwtAuthenticationFilter.java** - Request Filtering

**File Location:** `src/main/java/org/tp1ex2/projetspring/security/JwtAuthenticationFilter.java`

**How It Works:**

1. Intercepts every HTTP request
2. Looks for "Authorization: Bearer {token}" header
3. If token found:
   - Extracts username from token
   - For ADMIN tokens: Creates UserDetails in-memory without database lookup
   - For regular users: Loads UserDetails from database
   - Validates token signature and expiration
4. Sets authentication in SecurityContext if validation passes
5. Allows request to proceed to controller

**Request Flow:**

```
Request with Authorization header
        ↓
JwtAuthenticationFilter.doFilterInternal()
        ↓
Extract token from "Authorization: Bearer {token}"
        ↓
Is this an ADMIN token?
   YES → Create admin UserDetails in-memory
    NO → Load user from database
        ↓
Validate token (signature + expiration)
        ↓
Valid? → Set SecurityContext authentication → Allow request
Invalid? → Log error → Allow request to continue (no auth set)
```

#### 3. **SecurityConfig.java** - Spring Security Configuration

**File Location:** `src/main/java/org/tp1ex2/projetspring/security/SecurityConfig.java`

**Key Configurations:**

```java
// Disable CSRF (not needed for stateless JWT)
csrf().disable()

// Use stateless session management (no cookies/sessions)
sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

// Define public endpoints (no authentication required)
.requestMatchers("/", "/login", "/signup").permitAll()
.requestMatchers("/admin/login").permitAll()
.requestMatchers("/api/auth/**").permitAll()  // Auth endpoints public

// Define protected endpoints
.requestMatchers("/api/**").authenticated()     // All APIs need auth
.requestMatchers("/account/**").authenticated() // User accounts protected

// Add JWT filter
.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
```

**Beans Provided:**

```java
// Custom password encoder supporting plain text passwords
@Bean passwordEncoder() → PlainTextPasswordEncoder

// Authentication provider
@Bean authenticationProvider() → DaoAuthenticationProvider

// Authentication manager
@Bean authenticationManager() → AuthenticationManager
```

#### 4. **CustomUserDetailsService.java** - Load User Details

**File Location:** `src/main/java/org/tp1ex2/projetspring/security/CustomUserDetailsService.java`

**Purpose:** Implements Spring's `UserDetailsService` interface

```java
loadUserByUsername(String email)
- Searches database for user by email
- Creates Spring UserDetails object with:
  - username (email)
  - password
  - authorities (user type: CLIENT, GUIDE, etc.)
- Throws UsernameNotFoundException if user not found
```

#### 5. **PlainTextPasswordEncoder.java** - Password Encoding

**File Location:** `src/main/java/org/tp1ex2/projetspring/security/PlainTextPasswordEncoder.java`

**Implementation:**

```
Default behavior: Use BCrypt for new passwords

When matching password:
1. Check if encoded password looks like BCrypt (starts with $2)
2. If yes, use BCrypt matching
3. If no, fall back to plain text comparison (for backward compatibility)
```

**Why plain text?** This project uses plain text passwords for simplicity during development. In production, use proper bcrypt hashing.

### JWT Authentication Flow Diagram

```
┌─────────────────────────────────────────┐
│  1. User Login (POST /api/auth/login)   │
│     - Send email + password             │
└────────────────┬────────────────────────┘
                 │
                 ▼
         ┌──────────────────┐
         │  AuthRestCtrl    │
         │  Verify password │
         └────────┬─────────┘
                  │
         ┌────────▼────────┐
         │  Generate JWT   │
         │  (JwtUtil)      │
         └────────┬────────┘
                  │
                  ▼
    ┌─────────────────────────┐
    │ Return JWT Token to UI  │
    └────────┬────────────────┘
             │
             ▼
    ┌──────────────────────────────────┐
    │  2. Subsequent Requests          │
    │     Add: Authorization: Bearer {token}
    └──────────┬───────────────────────┘
               │
               ▼
    ┌───────────────────────────┐
    │ JwtAuthenticationFilter   │
    │ - Extract token           │
    │ - Validate & decode       │
    │ - Check expiration        │
    └──────────┬────────────────┘
               │
         ┌─────▴─────┐
         │           │
      VALID      INVALID
         │           │
         ▼           ▼
      Continue   Log Error
    REST ctrl   But continue
         │      (auth=null)
         ▼
    Check @RequestHeader
    Authorization
         │
         ▼
    Is Admin?
    ├─ YES → Allow API
    └─ NO  → 403 FORBIDDEN
```

---

## MVC ARCHITECTURE

### Model-View-Controller Pattern

This project uses **Spring MVC** with **Thymeleaf templating** for server-side rendering.

#### 1. **Model Layer** - Domain Objects

Data classes representing business entities:

**User.java** - Base user entity
```java
@Entity (JPA annotation - maps to database table)
@Inheritance(InheritanceType.SINGLE_TABLE) - Inheritance strategy
  Fields:
  - id (Primary Key, auto-generated)
  - firstName, lastName
  - email (unique)
  - password (plain text stored)
  - phoneNumber
  - type = "CLIENT" (default)
  
  Relationships:
  - One-to-Many with Tours (if guide)
  - One-to-Many with Reservations
  - One-to-Many with Reviews
  - One-to-Many with Favorites
```

**Guide.java** - Extends User
```java
@Entity
@DiscriminatorValue("GUIDE") - Single table inheritance discriminator
  Additional Fields:
  - bio (guide biography)
  - languages (List<String> - stored as collection)
  - toursCreated (List<Tour>)
  - rating (double, default 0.0)
```

**Place.java** - Tourist destination
```java
Fields:
- id (Primary Key)
- name, description
- pictureUrl, category
- latitude, longitude (coordinates)
- city
```

**Tour.java** - Tour package
```java
Fields:
- id (Primary Key)
- guide (Many-to-One with User)
- placeIds (List<Long> - collections of place IDs)
- name, description
- price, date, startingHour, endingHour
- latitude, longitude

Relationships:
- Many-to-One with Guide (User)
- One-to-Many with Reservations
- One-to-Many with Reviews
```

**Reservation.java** - User tour booking
```java
Fields:
- id (Primary Key)
- user (Many-to-One)
- tour (Many-to-One)
```

**Review.java** - User review/rating
```java
Fields:
- id (Primary Key)
- user (Many-to-One)
- tour (Many-to-One)
- reviewedEntityId, reviewedEntityType
- description, stars (1-5)
```

**Favorite.java** - User favorite place
```java
Fields:
- id (Primary Key)
- user (Many-to-One)
- place (Many-to-One)
```

**Admin.java** - Admin user
```java
Fields:
- id (Primary Key)
- login (username)
- password (plain text)
```

#### 2. **View Layer** - Thymeleaf Templates

Location: `src/main/resources/templates/`

**Public Pages:**
- `index.html` - Homepage with tour listings
- `login.html` - User login form
- `signup.html` - User registration form
- `account.html` - User account page
- `listplaces.html` - Browse all places
- `listusers.html` - View users
- `adduser.html`, `updateuser.html` - User CRUD forms
- `addplace.html`, `updateplace.html` - Place CRUD forms
- `error.html` - Error page

**Admin Pages:**
- `admin/login.html` - Admin login
- `admin/dashboard.html` - Admin dashboard with statistics
- `admin/tours/` - Tour management
- `admin/guides/` - Guide management
- `admin/reservations/` - Reservation management
- `admin/reviews/` - Review management
- `admin/favorites/` - Favorite management

**Template Syntax Example:**

```html
<!-- Conditional rendering -->
<div th:if="${isLoggedIn}">
  Welcome, <span th:text="${user.firstName}"></span>!
</div>

<!-- Form binding -->
<form th:action="@{/users/save}" method="post" th:object="${user}">
  <input type="text" th:field="*{firstName}" />
  <input type="text" th:field="*{email}" />
</form>

<!-- List rendering -->
<tr th:each="place : ${listPlaces}">
  <td th:text="${place.name}"></td>
  <td th:text="${place.category}"></td>
</tr>
```

#### 3. **Controller Layer** - MVC Controllers

Location: `src/main/java/org/tp1ex2/projetspring/controller/`

**HomeController** - Main application controller
```java
@Controller (not @RestController - returns views)

@GetMapping("/") 
- Displays homepage with places and tours
- Adds isLoggedIn flag to model

@GetMapping("/login")
- Shows login page
- Redirects to home if already logged in

@PostMapping("/login-process")
- Processes login form submission
- Finds user by email
- Verifies password (plain text comparison)
- Sets user in HttpSession
- Redirects to home page

@PostMapping("/signup-process")
- Creates new user account
- Saves user to database
- Sets user in session
```

**How Session-Based Login Works:**

```
1. User submits login form
2. Controller finds user by email
3. Compares provided password with stored password
4. If match: session.setAttribute("loggedInUser", user)
5. User object stored in server-side session (cookie-based)
6. Subsequent requests access via session.getAttribute("loggedInUser")
```

**PlaceController** - Place CRUD operations
```java
@GetMapping("/places/all")
- Lists all places

@GetMapping("/places/add")
- Shows add place form

@PostMapping("/places/save")
- Creates new place

@GetMapping("/places/edit/{id}")
- Shows edit form with place data

@PostMapping("/places/update/{id}")
- Updates existing place

@GetMapping("/places/delete/{id}")
- Deletes place
```

**UserController** - User management
```java
Similar CRUD operations for users
```

**AdminDashboardController** - Admin panel
```java
@GetMapping("/admin/login")
- Admin login page

@PostMapping("/admin/login-process")
- Admin login (session-based)

@GetMapping("/admin/dashboard")
- Admin dashboard (requires session login)
- Displays statistics:
  - User count
  - Guide count
  - Place count
  - Tour count
  - Reservation count
  - Review count
  - Favorite count

@PostMapping("/admin/tours/save")
@PostMapping("/admin/guides/save")
@PostMapping("/admin/reservations/save")
... (CRUD for all entities from dashboard)
```

---

## REST API ARCHITECTURE

### REST vs MVC in This Project

| Aspect | MVC (Thymeleaf) | REST API |
|--------|-----------------|----------|
| **URL Pattern** | `/users/all`, `/places/add` | `/api/user/all`, `/api/place/save` |
| **Returns** | HTML Pages (View) | JSON Data |
| **Authentication** | Session-based (cookies) | JWT Token-based |
| **Use Case** | Web browser interaction | Mobile/external app integration |
| **Statelessness** | Stateful (sessions) | Stateless (JWT) |

### REST Controllers

All REST controllers enforce **admin authentication** via JWT token.

#### **AuthRestController** - Authentication Endpoint

**File:** `src/main/java/org/tp1ex2/projetspring/controller/AuthRestController.java`

**Endpoints:**

```java
POST /api/auth/admin/login
- Request body:
  {
    "login": "admin",
    "password": "admin"
  }
- Response (200 OK):
  {
    "token": "eyJhbGc...",
    "type": "Bearer",
    "admin": {
      "id": 1,
      "login": "admin"
    }
  }
- Error (401): Invalid credentials

POST /api/auth/login
- Regular user login (similar structure)
```

**Why separate endpoints?**
- Admin uses `/api/auth/admin/login`
- Regular users use `/api/auth/login`
- Different token types generated (ADMIN vs regular)

#### **Protected REST Controllers**

All endpoints require: `Authorization: Bearer {token}` header

**Helper Method (in all controllers):**
```java
private boolean isAdminAuthenticated(String authHeader) {
  1. Extract token from "Bearer {token}"
  2. Use jwtUtil.isAdminToken(token) to check
  3. Return true if admin, false otherwise
  
  Every endpoint checks this at the beginning
}
```

#### **UserRestController** - User Management API

```java
@RestController
@RequestMapping("/api/user")

POST /api/user/save
- Requires admin JWT token
- Request body: User object (JSON)
- Creates new user in database
- Response: UserDTO (id, firstName, lastName, email, phoneNumber, type)
- Error (403): Not admin

GET /api/user/all
- Requires admin JWT token
- Response: List<UserDTO>

GET /api/user/getone/{id}
- Get specific user by ID
- Response: UserDTO

GET /api/user/email/{email}
- Get user by email address
- Response: UserDTO

PUT /api/user/update/{id}
- Update user information
- Request body: User object
- Response: Updated UserDTO

DELETE /api/user/{id}
- Delete user
- Response: 204 No Content
```

#### **PlaceRestController** - Place Management API

```java
POST /api/place/save - Create place
GET /api/place/all - List all places
GET /api/place/getone/{id} - Get place details
GET /api/place/category/{category} - Get by category
PUT /api/place/update/{id} - Update place
DELETE /api/place/{id} - Delete place
```

#### **TourRestController** - Tour Management API

```java
POST /api/tour/save - Create tour
GET /api/tour/all - List all tours
GET /api/tour/getone/{id} - Get tour details
GET /api/tour/guide/{guideId} - Get tours by guide
PUT /api/tour/update/{id} - Update tour
DELETE /api/tour/{id} - Delete tour
```

#### **GuideRestController** - Guide Management API

```java
POST /api/guide/save - Create guide (or convert user to guide)
GET /api/guide/all - List all guides
GET /api/guide/getone/{id} - Get guide details
GET /api/guide/rating/{minRating} - Get guides by minimum rating
PUT /api/guide/update/{id} - Update guide info
DELETE /api/guide/{id} - Delete guide
```

#### **ReservationRestController** - Booking Management API

```java
POST /api/reservation/save - Create reservation
GET /api/reservation/all - List all reservations
GET /api/reservation/getone/{id} - Get reservation details
GET /api/reservation/user/{userId} - Get user's reservations
PUT /api/reservation/update/{id} - Update reservation
DELETE /api/reservation/{id} - Cancel reservation
```

#### **ReviewRestController** - Review Management API

```java
POST /api/review/save - Create review
GET /api/review/all - List all reviews
GET /api/review/getone/{id} - Get review details
GET /api/review/user/{userId} - Get reviews by user
GET /api/review/tour/{tourId} - Get reviews for tour
PUT /api/review/update/{id} - Update review
DELETE /api/review/{id} - Delete review
```

#### **FavoriteRestController** - Favorite Management API

```java
POST /api/favorite/save - Add favorite
GET /api/favorite/all - List all favorites
GET /api/favorite/getone/{id} - Get favorite details
GET /api/favorite/user/{userId} - Get user's favorites
DELETE /api/favorite/{id} - Remove favorite
```

#### **AdminRestController** - Admin Management API

```java
POST /api/admin/save - Create new admin
GET /api/admin/all - List all admins
GET /api/admin/getone/{id} - Get admin by ID
GET /api/admin/login/{login} - Get admin by login
PUT /api/admin/update/{id} - Update admin
DELETE /api/admin/{id} - Delete admin
```

### REST API Response Codes

| Code | Meaning | Example |
|------|---------|---------|
| 200 | Success | GET request returns data |
| 201 | Created | POST creates resource successfully |
| 204 | No Content | DELETE successful (empty response) |
| 400 | Bad Request | Invalid data sent |
| 401 | Unauthorized | No valid JWT token |
| 403 | Forbidden | Authenticated but not admin |
| 404 | Not Found | Resource doesn't exist |
| 500 | Server Error | Unexpected error |

---

## DATABASE MODELS & RELATIONSHIPS

### Entity-Relationship Diagram

```
┌─────────────────────────────────────┐
│            User (dtype=USER)        │
├─────────────────────────────────────┤
│ id (PK)                             │
│ firstName, lastName, email, password│
│ phoneNumber, type = 'CLIENT'        │
└──┬──────────────────────────────────┘
   │
   │ Single Table Inheritance
   ▼
┌─────────────────────────────────────┐
│          Guide (dtype=GUIDE)        │ extends User
├─────────────────────────────────────┤
│ Additional Fields:                  │
│ bio, languages, rating              │
└─────────────────────────────────────┘

┌──────────────────────────────┐
│        Place                 │
├──────────────────────────────┤
│ id (PK), name, description   │
│ category, pictureUrl         │
│ latitude, longitude, city    │
└──────────────────────────────┘

┌──────────────────────────────┐    ┌──────────────────┐
│        Tour                  │◄───┤ Guide/User       │
├──────────────────────────────┤    └──────────────────┘
│ id (PK), name, description   │    (Many-to-One)
│ price, date, startingHour    │
│ endingHour, latitude, long   │
│ guide_id (FK)                │
│ placeIds (List<Long>)        │
└──────────────────────────────┘

┌──────────────────────────────┐    ┌──────────────────┐
│      Reservation             │────┤ Tour             │
├──────────────────────────────┤    └──────────────────┘
│ id (PK)                      │
│ user_id (FK)                 │    ┌──────────────────┐
│ tour_id (FK)                 │────┤ User             │
└──────────────────────────────┘    └──────────────────┘

┌──────────────────────────────┐    ┌──────────────────┐
│        Review                │────┤ Tour             │
├──────────────────────────────┤    └──────────────────┘
│ id (PK)                      │
│ user_id (FK)                 │    ┌──────────────────┐
│ tour_id (FK)                 │────┤ User             │
│ reviewedEntityId             │
│ reviewedEntityType           │
│ description, stars           │
└──────────────────────────────┘

┌──────────────────────────────┐    ┌──────────────────┐
│       Favorite               │────┤ Place            │
├──────────────────────────────┤    └──────────────────┘
│ id (PK)                      │
│ user_id (FK)                 │    ┌──────────────────┐
│ place_id (FK)                │────┤ User             │
└──────────────────────────────┘    └──────────────────┘

┌──────────────────────────────┐
│        Admin                 │
├──────────────────────────────┤
│ id (PK), login, password     │
└──────────────────────────────┘
```

### Relationship Types

**1. One-to-Many: User → Tours**
```
User (1) ----< Tours (Many)
If user is a guide, they can create multiple tours
```

**2. One-to-Many: User → Reservations**
```
User (1) ----< Reservations (Many)
A user can have multiple tour reservations
```

**3. One-to-Many: User → Reviews**
```
User (1) ----< Reviews (Many)
A user can leave multiple reviews
```

**4. One-to-Many: User → Favorites**
```
User (1) ----< Favorites (Many)
A user can favorite multiple places
```

**5. One-to-Many: Tour → Reservations**
```
Tour (1) ----< Reservations (Many)
A tour can have multiple reservations from different users
```

**6. One-to-Many: Tour → Reviews**
```
Tour (1) ----< Reviews (Many)
A tour can receive multiple reviews
```

**7. Many-to-One: Reservation**
```
Reservation → User (Many-to-One)
Reservation → Tour (Many-to-One)
```

**8. Many-to-One: Review**
```
Review → User (Many-to-One)
Review → Tour (Many-to-One)
```

**9. Many-to-One: Favorite**
```
Favorite → User (Many-to-One)
Favorite → Place (Many-to-One)
```

**10. Single Table Inheritance: User → Guide**
```
User table with discriminator column 'dtype'
- dtype='USER' → Regular user
- dtype='GUIDE' → Guide (subclass)
```

### Database Configuration

**File:** `src/main/resources/application.properties`

```properties
# MySQL Connection
spring.datasource.url=jdbc:mysql://localhost:3306/tuniwayspring?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=          # Empty for local dev
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Hibernate JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update  # Auto-create/update tables

# Logging & Debugging
spring.jpa.show-sql=true              # Log SQL queries
spring.jpa.properties.hibernate.format_sql=true

# Server Configuration
server.port=8082
```

**Key Concepts:**

- `ddl-auto=update` automatically creates and modifies tables based on entity definitions
- `show-sql=true` logs all SQL queries (development only)
- `createDatabaseIfNotExist=true` creates database if it doesn't exist

---

## CORE COMPONENTS

### Service Layer Architecture

#### Service Interface Pattern

Each entity has:
- **Interface** (contract) - defines operations
- **Implementation** (with @Service) - implements operations

**Example: UserService Interface**

```java
package org.tp1ex2.projetspring.service;

public interface UserService {
    User create(User user);
    User createUser(User user);
    List<User> getAll();
    List<User> getAllUsers();
    User getById(Long id);
    User getUserById(Long id);
    User update(User user);
    User updateUser(User user);
    void delete(Long id);
    void deleteUser(Long id);
    User findByEmail(String email);
}
```

**Example: UserServiceImpl Implementation**

```java
@Service
@Transactional  // Auto-commit/rollback for methods
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }
    
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    @Override
    public User updateUser(User user) {
        return userRepository.saveAndFlush(user);
    }
    
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    // Delegate methods (both names for compatibility)
    @Override
    public User create(User user) {
        return createUser(user);
    }
    // ... etc
}
```

#### All Services Provided

1. **UserService/UserServiceImpl** - User CRUD
2. **GuideService/GuideServiceImpl** - Guide-specific operations
3. **PlaceService/PlaceServiceImpl** - Place management
4. **TourService/TourServiceImpl** - Tour operations
5. **ReservationService/ReservationServiceImpl** - Booking management
6. **ReviewService/ReviewServiceImpl** - Review/rating operations
7. **FavoriteService/FavoriteServiceImpl** - Favorite management
8. **AdminService/AdminServiceImpl** - Admin operations

### Repository Layer Architecture

#### Repository Pattern

Uses Spring Data JPA for data access:

```java
public interface UserRepository extends JpaRepository<User, Long> {
    // JpaRepository provides CRUD methods:
    // save(T entity)
    // findById(ID id)
    // findAll()
    // delete(T entity)
    // deleteById(ID id)
    
    // Custom query method
    User findByEmail(String email);
}
```

**All Repositories:**
- `UserRepository` - extends JpaRepository<User, Long>
- `GuideRepository` - extends JpaRepository<Guide, Long>
- `PlaceRepository` - extends JpaRepository<Place, Long>
- `TourRepository` - extends JpaRepository<Tour, Long>
- `ReservationRepository` - extends JpaRepository<Reservation, Long>
- `ReviewRepository` - extends JpaRepository<Review, Long>
- `FavoriteRepository` - extends JpaRepository<Favorite, Long>
- `AdminRepository` - extends JpaRepository<Admin, Long>
  - Custom: `findByLogin(String login)`

### Dependency Injection

**@Autowired Pattern:**

```java
@Service
public class UserServiceImpl {
    
    @Autowired
    private UserRepository userRepository;  // Auto-injected by Spring
}
```

Spring Container:
1. Detects @Service and @Repository annotations
2. Creates instances (beans)
3. Injects dependencies where @Autowired is used
4. Manages lifecycle

### Transaction Management

**@Transactional Annotation:**

```java
@Transactional
public void deleteUser(Long id) {
    // If any exception occurs: automatic ROLLBACK
    // If completes successfully: automatic COMMIT
    userRepository.deleteById(id);
}
```

Benefits:
- Automatic commit on success
- Automatic rollback on exception
- Data consistency guaranteed

### DTO (Data Transfer Object) Pattern

**UserDTO** - Lightweight data object

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String type;
}
```

**Why DTOs?**
- Don't expose internal entity structure
- Reduce data transfer size
- Separate API contract from entity structure
- Hide sensitive fields (like password)

**Conversion Method in Controller:**

```java
private UserDTO convertToDTO(User user) {
    if (user == null) return null;
    return new UserDTO(user.getId(), user.getFirstName(), 
                      user.getLastName(), user.getEmail(), 
                      user.getPhoneNumber(), user.getType());
}
```

---

## AUTHENTICATION FLOW

### Complete Login & Authorization Flow

#### **Scenario 1: Admin REST API Login**

```
CLIENT
  ↓
POST /api/auth/admin/login
{
  "login": "admin",
  "password": "admin"
}
  ↓
AuthRestController.adminLogin()
  ├─ Find admin in database
  ├─ Compare password (plain text)
  ├─ Generate JWT using JwtUtil
  │   ├─ Create claims with type="ADMIN"
  │   ├─ Sign with secret key (HMAC-SHA256)
  │   └─ Set 24-hour expiration
  ├─ Return token
  └─ Response:
     {
       "token": "eyJhbGciOiJIUzI1NiJ9...",
       "type": "Bearer",
       "admin": {"id": 1, "login": "admin"}
     }

CLIENT stores token in memory/storage
```

#### **Scenario 2: Admin REST API Request**

```
CLIENT
  ↓
GET /api/user/all
Headers: {
  Authorization: "Bearer eyJhbGciOiJIUzI1NiJ9..."
}
  ↓
JwtAuthenticationFilter.doFilterInternal()
  ├─ Extract token from header
  ├─ Check if it's admin token
  ├─ Load admin UserDetails in-memory
  ├─ Validate token signature & expiration
  └─ Set SecurityContextHolder authentication

  ↓
UserRestController
  ├─ Call isAdminAuthenticated(authHeader)
  ├─ Check jwtUtil.isAdminToken(token)
  ├─ If true: proceed with operation
  └─ If false: return 403 FORBIDDEN

  ↓
Response: List<UserDTO> (200 OK)
```

#### **Scenario 3: Regular User MVC Login**

```
CLIENT (Web Browser)
  ↓
GET /login → Shows login.html form
  ↓
POST /login-process
{
  email: user@example.com
  password: password123
}
  ↓
HomeController.loginProcess()
  ├─ Find user by email
  ├─ Compare password
  └─ If match:
     ├─ session.setAttribute("loggedInUser", user)
     ├─ Cookie created by servlet container
     └─ Redirect to /

  ↓
Browser stores session cookie automatically
Subsequent requests include cookie in requests
```

#### **Scenario 4: Session Check in MVC**

```
CLIENT (Web Browser)
  ↓
GET /account
  ↓
HomeController.accountPage()
  ├─ Get from session: (User) session.getAttribute("loggedInUser")
  ├─ If null: redirect to /login
  └─ If present:
     ├─ Add user to model
     └─ Render account.html

  ↓
Response: HTML page with user info
```

---

## API ENDPOINTS

### Complete API Reference

#### Authentication Endpoints

```
POST /api/auth/admin/login
  Admin login via REST
  Body: {"login": "admin", "password": "admin"}
  Response: {token, type, admin}
  Auth: None (public)

POST /api/auth/login
  User login via REST
  Body: {"email": "user@example.com", "password": "pass"}
  Response: {token, type, user}
  Auth: None (public)
```

#### User API

```
POST /api/user/save
  Create user
  Body: User object
  Response: UserDTO
  Auth: Admin JWT

GET /api/user/all
  List all users
  Response: List<UserDTO>
  Auth: Admin JWT

GET /api/user/getone/{id}
  Get user by ID
  Response: UserDTO
  Auth: Admin JWT

GET /api/user/email/{email}
  Get user by email
  Response: UserDTO
  Auth: Admin JWT

PUT /api/user/update/{id}
  Update user
  Body: User object
  Response: UserDTO
  Auth: Admin JWT

DELETE /api/user/{id}
  Delete user
  Response: 204 No Content
  Auth: Admin JWT
```

#### Place API

```
POST /api/place/save
  Create place
  Auth: Admin JWT

GET /api/place/all
  List all places
  Auth: Admin JWT

GET /api/place/getone/{id}
  Get place by ID
  Auth: Admin JWT

PUT /api/place/update/{id}
  Update place
  Auth: Admin JWT

DELETE /api/place/{id}
  Delete place
  Auth: Admin JWT
```

#### Tour API

```
POST /api/tour/save
  Create tour
  Auth: Admin JWT

GET /api/tour/all
  List all tours
  Auth: Admin JWT

GET /api/tour/getone/{id}
  Get tour by ID
  Auth: Admin JWT

GET /api/tour/guide/{guideId}
  Get tours by guide
  Auth: Admin JWT

PUT /api/tour/update/{id}
  Update tour
  Auth: Admin JWT

DELETE /api/tour/{id}
  Delete tour
  Auth: Admin JWT
```

#### Guide API

```
POST /api/guide/save
  Create/add guide
  Auth: Admin JWT

GET /api/guide/all
  List all guides
  Auth: Admin JWT

GET /api/guide/getone/{id}
  Get guide by ID
  Auth: Admin JWT

GET /api/guide/rating/{minRating}
  Get guides by rating
  Auth: Admin JWT

PUT /api/guide/update/{id}
  Update guide
  Auth: Admin JWT

DELETE /api/guide/{id}
  Delete guide
  Auth: Admin JWT
```

#### Reservation API

```
POST /api/reservation/save
  Create reservation
  Auth: Admin JWT

GET /api/reservation/all
  List all reservations
  Auth: Admin JWT

GET /api/reservation/getone/{id}
  Get reservation by ID
  Auth: Admin JWT

GET /api/reservation/user/{userId}
  Get user's reservations
  Auth: Admin JWT

PUT /api/reservation/update/{id}
  Update reservation
  Auth: Admin JWT

DELETE /api/reservation/{id}
  Cancel reservation
  Auth: Admin JWT
```

#### Review API

```
POST /api/review/save
  Create review
  Auth: Admin JWT

GET /api/review/all
  List all reviews
  Auth: Admin JWT

GET /api/review/getone/{id}
  Get review by ID
  Auth: Admin JWT

GET /api/review/user/{userId}
  Get reviews by user
  Auth: Admin JWT

GET /api/review/tour/{tourId}
  Get reviews for tour
  Auth: Admin JWT

PUT /api/review/update/{id}
  Update review
  Auth: Admin JWT

DELETE /api/review/{id}
  Delete review
  Auth: Admin JWT
```

#### Favorite API

```
POST /api/favorite/save
  Add favorite
  Auth: Admin JWT

GET /api/favorite/all
  List all favorites
  Auth: Admin JWT

GET /api/favorite/getone/{id}
  Get favorite by ID
  Auth: Admin JWT

GET /api/favorite/user/{userId}
  Get user's favorites
  Auth: Admin JWT

DELETE /api/favorite/{id}
  Remove favorite
  Auth: Admin JWT
```

#### Admin API

```
POST /api/admin/save
  Create admin
  Auth: Admin JWT

GET /api/admin/all
  List all admins
  Auth: Admin JWT

GET /api/admin/getone/{id}
  Get admin by ID
  Auth: Admin JWT

GET /api/admin/login/{login}
  Get admin by login
  Auth: Admin JWT

PUT /api/admin/update/{id}
  Update admin
  Auth: Admin JWT

DELETE /api/admin/{id}
  Delete admin
  Auth: Admin JWT
```

---

## SECURITY FEATURES

### 1. JWT Token Security

**Components:**
- **Algorithm:** HMAC-SHA256 (symmetric signing)
- **Secret Key:** 256-bit encryption key stored in properties file
- **Expiration:** 24 hours (86400000 ms)
- **Claims:** username, type (ADMIN/USER), issued time, expiration

**Token Validation:**
- Signature verification (ensures token not tampered with)
- Expiration check (ensures token not expired)
- Username verification (ensures token matches request)

**Vulnerabilities & Mitigations:**

| Vulnerability | Mitigation |
|---------------|-----------|
| Token forgery | HMAC-SHA256 signature prevents modification |
| Expired tokens | Expiration check rejects old tokens |
| Token replay | Can't reuse token after expiration |
| Token theft | Use HTTPS for transmission, store securely |

### 2. Authentication Methods

**Dual Authentication System:**

```
MVC (Web App)
├─ Session-based (HttpSession)
├─ Cookie storage
├─ Stateful
└─ Better for web browsers

REST API
├─ JWT token-based
├─ Header storage (Authorization: Bearer)
├─ Stateless
└─ Better for mobile/external apps
```

### 3. Admin Token Detection

**Security Check:**

```java
public Boolean isAdminToken(String token)
  ├─ Extract "type" claim from token
  ├─ Check if type == "ADMIN"
  └─ Return true/false
```

**Purpose:**
- Distinguish admin from regular user tokens
- Enforce admin-only endpoints
- Prevent privilege escalation

### 4. Authorization Checks

**Every REST Endpoint:**

```java
private boolean isAdminAuthenticated(String authHeader) {
    // 1. Check header exists
    // 2. Verify Bearer prefix
    // 3. Extract token
    // 4. Validate token is admin token
    // 5. Catch any exceptions (invalid token)
}

@PostMapping("/save")
public ResponseEntity<?> saveEntity(@RequestBody Entity entity,
                                    @RequestHeader(...) String auth) {
    if (!isAdminAuthenticated(auth)) {
        return ResponseEntity.status(403).build();  // FORBIDDEN
    }
    // Proceed with operation
}
```

**Result:**
- 200 OK: Success (authenticated admin)
- 403 FORBIDDEN: Not admin (authenticated non-admin or invalid token)
- 401 UNAUTHORIZED: No valid token (handled by Spring Security)

### 5. Password Security

**Current Implementation:**
```java
PlainTextPasswordEncoder
├─ Stores passwords as plain text in database (NOT SECURE)
├─ Used for development/backward compatibility
└─ Uses BCrypt when available
```

**Issues:**
- ⚠️ Passwords visible in database
- ⚠️ Easy to reverse if database compromised
- ⚠️ Not suitable for production

**Production Recommendation:**
```java
// Use Spring's BCryptPasswordEncoder directly
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

### 6. CSRF Protection

**Disabled in REST API:**
```java
http.csrf(csrf -> csrf.disable())
```

**Reason:**
- JWT is stateless, CSRF tokens not needed
- CSRF exploits cookie-based sessions
- JWT tokens verified via signature

**Enabled for MVC:**
- Session-based authentication
- CSRF tokens protect form submissions

### 7. Session Management

**MVC (Stateful):**
```java
sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATEFUL)
├─ Sessions created for logged-in users
├─ Stored server-side
├─ Cookie stores session ID
└─ Vulnerable to session hijacking if HTTPS not used
```

**REST (Stateless):**
```java
sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
├─ No sessions created
├─ Tokens are self-contained
├─ No server-side state
└─ Scalable (no session replication needed)
```

### 8. Default Admin User

**Initialization:**

File: `src/main/java/org/tp1ex2/projetspring/config/AdminDataInitializer.java`

```java
@Component
public class AdminDataInitializer implements CommandLineRunner {
    
    @Override
    public void run(String... args) {
        // Checks if admin exists
        Admin existing = adminRepository.findByLogin("admin");
        
        if (existing == null) {
            // Create default admin
            Admin admin = new Admin();
            admin.setLogin("admin");
            admin.setPassword("admin");  // Default password
            adminRepository.save(admin);
        }
    }
}
```

**Security Implications:**
- Default credentials (admin/admin) only in development
- **MUST change in production**
- Run once on application startup

---

## DEPLOYMENT & CONFIGURATION

### Build Configuration

**File:** `build.gradle`

```gradle
plugins {
    id 'org.springframework.boot' version '4.0.0'
}

dependencies {
    // Core Spring Boot
    implementation 'org.springframework.boot:spring-boot-starter-webmvc'
    implementation 'org.springframework.boot:spring-boot-starter-security'
    
    // Database
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    runtimeOnly 'com.mysql:mysql-connector-j'
    
    // JWT
    implementation 'io.jsonwebtoken:jjwt-api:0.12.3'
    runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.12.3'
    runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.12.3'
    
    // Templates
    implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
    
    // Utilities
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'
}
```

### Running the Application

**Option 1: Gradle Wrapper**
```bash
./gradlew bootRun
```

**Option 2: Build & Run**
```bash
./gradlew build
java -jar build/libs/projetspring-0.0.1-SNAPSHOT.jar
```

**Application Startup:**
1. Spring loads application.properties
2. AdminDataInitializer runs (creates default admin if needed)
3. Database tables created/updated via Hibernate
4. Application listens on `http://localhost:8082`

### Directory Structure

```
projetspringboot/
├── src/
│   ├── main/
│   │   ├── java/org/tp1ex2/projetspring/
│   │   │   ├── ProjetspringApplication.java (Entry point)
│   │   │   ├── controller/ (MVC & REST)
│   │   │   ├── service/ (Interfaces)
│   │   │   ├── implementation/ (Service impls)
│   │   │   ├── model/ (Entity classes)
│   │   │   ├── repository/ (Data access)
│   │   │   ├── security/ (JWT & Auth)
│   │   │   ├── config/ (Configuration)
│   │   │   └── dto/ (Data Transfer Objects)
│   │   └── resources/
│   │       ├── application.properties (Config)
│   │       └── templates/ (Thymeleaf HTML)
│   └── test/
│       └── java/ (Unit tests)
├── build.gradle (Gradle config)
└── README.md (Documentation)
```

---

## SUMMARY: How Everything Works Together

### Request Flow Example: Create New Tour

**Via REST API (Admin)**

```
1. ADMIN CLIENT
   │
   ├─ POST /api/auth/admin/login
   │  ├─ AuthRestController.adminLogin()
   │  ├─ JwtUtil.generateToken("admin", "ADMIN")
   │  └─ Return JWT token
   │
   └─ Store token locally

2. ADMIN CLIENT
   │
   ├─ POST /api/tour/save
   │  Header: Authorization: Bearer {token}
   │  Body: {"name": "Paris Tour", "price": 100, ...}
   │
   └─ JwtAuthenticationFilter
      ├─ Extract token from header
      ├─ Validate signature & expiration
      ├─ Check if admin token
      └─ Set SecurityContext authentication

3. TourRestController.saveTour()
   │
   ├─ isAdminAuthenticated(authHeader) checks token
   ├─ If valid:
   │  ├─ TourService.createTour(tour)
   │  │  └─ TourRepository.save(tour)
   │  │     └─ Hibernate inserts into database
   │  │        INSERT INTO tour (name, price, ...) VALUES (...)
   │  │
   │  └─ Return 201 CREATED with Tour object
   │
   └─ If invalid: Return 403 FORBIDDEN

4. DATABASE
   │
   ├─ tour table updated with new row
   └─ User (guide) foreign key set

5. RESPONSE
   │
   └─ Client receives:
      {
        "id": 1,
        "name": "Paris Tour",
        "price": 100,
        ...
      }
```

### Request Flow Example: View Places (MVC)

```
1. USER BROWSER
   │
   ├─ GET /places/all
   │
   └─ PlaceController.getAllPlaces()

2. GET FROM SESSION
   │
   ├─ User user = (User) session.getAttribute("loggedInUser")
   ├─ If null: page shows "not logged in"
   └─ If exists: show user info

3. SERVICE LAYER
   │
   ├─ PlaceService.getAllPlaces()
   │  └─ PlaceRepository.findAll()
   │     └─ SELECT * FROM place

4. DATABASE
   │
   └─ Returns List<Place>

5. MODEL + VIEW
   │
   ├─ Add to model: model.addAttribute("places", places)
   ├─ Add to model: model.addAttribute("user", loggedInUser)
   │
   └─ Return "listplaces.html" (Thymeleaf template)

6. THYMELEAF RENDERING
   │
   ├─ ${places} → iterate and display each place
   ├─ ${user} → show logged-in user info
   └─ Generate HTML

7. RESPONSE
   │
   └─ Browser receives rendered HTML
      <h1>Places</h1>
      <table>
        <tr><td>Place 1</td><td>Category</td></tr>
        ...
      </table>
      Welcome, John!
```

---

## KEY TECHNOLOGY STACK

| Component | Technology | Version |
|-----------|-----------|---------|
| Framework | Spring Boot | 4.0.0 |
| Java Version | Java | 17+ |
| Web | Spring MVC + Thymeleaf | Latest |
| Security | Spring Security + JWT | 0.12.3 (jjwt) |
| Database | MySQL + Hibernate ORM | 8.0+ |
| Build Tool | Gradle | 7.0+ |
| Authentication | JWT (HMAC-SHA256) | - |
| Session (MVC) | HttpSession (Servlet) | - |
| Utilities | Lombok | Latest |

---

## CONCLUSION

This Spring Boot project implements a **complete tour booking platform** with:

✅ **Dual Authentication System:**
- Session-based MVC for web browsing
- JWT-based REST API for mobile/external apps

✅ **Robust Security:**
- JWT token validation (signature + expiration)
- Admin-only endpoint protection
- Session management

✅ **Layered Architecture:**
- Controller → Service → Repository → Database
- Clean separation of concerns
- Easy to maintain and extend

✅ **Rich Entity Model:**
- Users, Guides, Places, Tours, Reservations, Reviews, Favorites
- Complex relationships (One-to-Many, Many-to-One)
- Single-table inheritance for Guide (extends User)

✅ **Comprehensive CRUD Operations:**
- Full REST API for all entities
- MVC web interface for browsing and management
- Admin dashboard for system control

The architecture is production-ready but needs security hardening:
- Use BCrypt for password hashing
- Enable HTTPS for token transmission
- Change default admin credentials
- Implement rate limiting
- Add input validation and sanitization

---

**End of Technical Guide**
