# Tuniway API Testing with Postman Guide

## Application Details
- **Base URL**: `http://localhost:8082`
- **Port**: 8082
- **Database**: MySQL (tuniwayspring)

## 1. Authentication Flow (Session-Based)

### Step 1: Sign Up New User
**Endpoint**: `POST /signup-process`

**Headers**:
```
Content-Type: application/x-www-form-urlencoded
```

**Form Data (Body)**:
```
firstName=John
lastName=Doe
email=john.doe@example.com
phoneNumber=+1234567890
password=password123
confirmPassword=password123
type=CLIENT
```

**Expected Response**: 
- Status: 302 (Redirect to /account)
- Session cookie set with user data

---

### Step 2: Login with Email/Password
**Endpoint**: `POST /login-process`

**Headers**:
```
Content-Type: application/x-www-form-urlencoded
```

**Form Data (Body)**:
```
email=john.doe@example.com
password=password123
```

**Expected Response**:
- Status: 302 (Redirect to /account)
- Session cookie set

---

## 2. User Account Management (Requires Session)

### Get User Account Page
**Endpoint**: `GET /account`

**Headers**:
```
Cookie: JSESSIONID=<your_session_id>
```

**Expected Response**:
- Status: 200
- HTML page with user profile, favorites, reservations, and reviews

---

### Update User Profile
**Endpoint**: `POST /account/update`

**Headers**:
```
Content-Type: application/x-www-form-urlencoded
Cookie: JSESSIONID=<your_session_id>
```

**Form Data (Body)**:
```
firstName=John
lastName=Smith
email=john.smith@example.com
phoneNumber=+9876543210
password=newpassword123
```

**Expected Response**:
- Status: 302 (Redirect to /account)

---

### Remove Favorite
**Endpoint**: `POST /account/favorite/delete`

**Headers**:
```
Content-Type: application/x-www-form-urlencoded
Cookie: JSESSIONID=<your_session_id>
```

**Form Data (Body)**:
```
favoriteId=1
```

**Expected Response**:
- Status: 302 (Redirect to /account)

---

### Cancel Reservation
**Endpoint**: `POST /account/reservation/cancel`

**Headers**:
```
Content-Type: application/x-www-form-urlencoded
Cookie: JSESSIONID=<your_session_id>
```

**Form Data (Body)**:
```
reservationId=1
```

**Expected Response**:
- Status: 302 (Redirect to /account)

---

### Delete Review
**Endpoint**: `POST /account/review/delete`

**Headers**:
```
Content-Type: application/x-www-form-urlencoded
Cookie: JSESSIONID=<your_session_id>
```

**Form Data (Body)**:
```
reviewId=1
```

**Expected Response**:
- Status: 302 (Redirect to /account)

---

## 3. REST API Endpoints (No Session Required)

### Users API

#### Create User
**Endpoint**: `POST /api/user/save`

**Headers**:
```
Content-Type: application/json
```

**Body**:
```json
{
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "jane.smith@example.com",
  "phoneNumber": "+1234567890",
  "password": "password123",
  "type": "CLIENT"
}
```

**Expected Response**:
- Status: 201 (Created)
- Returns User object with ID

---

#### Get All Users
**Endpoint**: `GET /api/user/all`

**Expected Response**:
- Status: 200
- Array of all users

---

#### Get User by ID
**Endpoint**: `GET /api/user/getone/{id}`

Example: `GET /api/user/getone/1`

**Expected Response**:
- Status: 200
- User object (if found) or 404 (if not found)

---

#### Find User by Email
**Endpoint**: `GET /api/user/email/{email}`

Example: `GET /api/user/email/john.doe@example.com`

**Expected Response**:
- Status: 200
- User object (if found)

---

#### Update User
**Endpoint**: `PUT /api/user/update/1`

**Headers**:
```
Content-Type: application/json
```

**Body**:
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Updated",
  "email": "john.updated@example.com",
  "phoneNumber": "+1234567890",
  "password": "password123",
  "type": "CLIENT"
}
```

**Expected Response**:
- Status: 200
- Updated User object

---

#### Delete User
**Endpoint**: `DELETE /api/user/1`

**Expected Response**:
- Status: 204 (No Content)

---

### Places API

#### Create Place
**Endpoint**: `POST /api/place/save`

**Headers**:
```
Content-Type: application/json
```

**Body**:
```json
{
  "name": "Eiffel Tower",
  "category": "Landmark",
  "city": "Paris",
  "latitude": 48.8584,
  "longitude": 2.2945,
  "description": "Famous French landmark"
}
```

**Expected Response**:
- Status: 201 (Created)

---

#### Get All Places
**Endpoint**: `GET /api/place/all`

**Expected Response**:
- Status: 200
- Array of all places

---

#### Get Place by ID
**Endpoint**: `GET /api/place/getone/1`

**Expected Response**:
- Status: 200
- Place object

---

#### Get Places by Category
**Endpoint**: `GET /api/place/category/Landmark`

**Expected Response**:
- Status: 200
- Array of places in that category

---

#### Update Place
**Endpoint**: `PUT /api/place/update/1`

**Headers**:
```
Content-Type: application/json
```

**Body**:
```json
{
  "id": 1,
  "name": "Updated Name",
  "category": "Landmark",
  "city": "Paris",
  "latitude": 48.8584,
  "longitude": 2.2945,
  "description": "Updated description"
}
```

---

#### Delete Place
**Endpoint**: `DELETE /api/place/1`

---

### Tours API

#### Create Tour
**Endpoint**: `POST /api/tour/save`

**Headers**:
```
Content-Type: application/json
```

**Body**:
```json
{
  "guide": {
    "id": 1
  },
  "placeIds": [1, 2, 3],
  "name": "Paris City Tour",
  "description": "Explore the best of Paris",
  "price": 99.99,
  "date": "2025-12-25",
  "startingHour": "09:00",
  "endingHour": "17:00",
  "latitude": 48.8566,
  "longitude": 2.3522
}
```

---

#### Get All Tours
**Endpoint**: `GET /api/tour/all`

---

#### Get Tours by Guide
**Endpoint**: `GET /api/tour/guide/1`

---

### Reservations API

#### Create Reservation
**Endpoint**: `POST /api/reservation/save`

**Headers**:
```
Content-Type: application/json
```

**Body**:
```json
{
  "user": {
    "id": 1
  },
  "tour": {
    "id": 1
  },
  "status": "CONFIRMED"
}
```

---

#### Get All Reservations
**Endpoint**: `GET /api/reservation/all`

---

#### Get Reservations by User
**Endpoint**: `GET /api/reservation/user/1`

---

### Reviews API

#### Create Review
**Endpoint**: `POST /api/review/save`

**Headers**:
```
Content-Type: application/json
```

**Body**:
```json
{
  "user": {
    "id": 1
  },
  "tour": {
    "id": 1
  },
  "stars": 5,
  "description": "Amazing tour! Highly recommended."
}
```

---

#### Get All Reviews
**Endpoint**: `GET /api/review/all`

---

#### Get Reviews by User
**Endpoint**: `GET /api/review/user/1`

---

#### Get Reviews by Tour
**Endpoint**: `GET /api/review/tour/1`

---

### Favorites API

#### Create Favorite
**Endpoint**: `POST /api/favorite/save`

**Headers**:
```
Content-Type: application/json
```

**Body**:
```json
{
  "user": {
    "id": 1
  },
  "place": {
    "id": 1
  }
}
```

---

#### Get All Favorites
**Endpoint**: `GET /api/favorite/all`

---

#### Get Favorites by User
**Endpoint**: `GET /api/favorite/user/1`

---

### Guides API

#### Create Guide
**Endpoint**: `POST /api/guide/save`

**Headers**:
```
Content-Type: application/json
```

**Body**:
```json
{
  "firstName": "Marco",
  "lastName": "Rossi",
  "email": "marco@guides.com",
  "phoneNumber": "+39123456789",
  "password": "guide123",
  "type": "GUIDE",
  "bio": "Experienced tour guide with 10 years experience",
  "languages": ["Italian", "English", "French"],
  "rating": 4.8
}
```

---

#### Get All Guides
**Endpoint**: `GET /api/guide/all`

---

#### Get Guides by Rating
**Endpoint**: `GET /api/guide/rating/4.5`

Returns guides with rating >= 4.5

---

### Admin API

#### Admin Login
**Endpoint**: `POST /api/admin/login`

**Headers**:
```
Content-Type: application/json
```

**Body**:
```json
{
  "login": "admin",
  "password": "admin123"
}
```

---

## Testing Steps in Postman

### 1. Create a Guide First
- Use `POST /api/guide/save`
- Note the guide ID from response

### 2. Create Some Places
- Use `POST /api/place/save` (multiple times)
- Note the place IDs

### 3. Create a Tour
- Use `POST /api/tour/save` with the guide ID and place IDs

### 4. Create a Regular User
- Use `POST /api/user/save`
- Note the user ID

### 5. Create a Reservation
- Use `POST /api/reservation/save` with user and tour IDs

### 6. Create a Review
- Use `POST /api/review/save` with user and tour IDs

### 7. Create a Favorite
- Use `POST /api/favorite/save` with user and place IDs

### 8. Test Authentication
- Use `POST /signup-process` to create a user via web form
- Use `POST /login-process` to log in
- Check the session cookie in Postman

## Troubleshooting

### Issue: 500 Error on Login/Signup
**Solution**: The `-parameters` compiler flag has been added. Make sure to rebuild the project with `.\gradlew clean build`.

### Issue: Parameter name not found
**Solution**: Ensure the request body uses correct parameter names (case-sensitive).

### Issue: Session not persisting
**Solution**: Postman should automatically save cookies. Check "Manage Cookies" in Postman settings to enable cookie handling.

### Issue: 404 Not Found
**Solution**: Verify the endpoint URL matches exactly and check that you're using the correct HTTP method (GET, POST, PUT, DELETE).
