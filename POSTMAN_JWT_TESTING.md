# Postman JWT Testing Guide

## Base URL
```
http://localhost:8082
```

## Step-by-Step Testing

### Step 1: Register a New User

**Request:**
- **Method:** `POST`
- **URL:** `http://localhost:8082/api/auth/register`
- **Headers:**
  ```
  Content-Type: application/json
  ```
- **Body (raw JSON):**
  ```json
  {
    "email": "testuser@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe",
    "phoneNumber": "+1234567890",
    "type": "CLIENT"
  }
  ```

**Expected Response (201 Created):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "user": {
    "id": 1,
    "email": "testuser@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "type": "CLIENT"
  }
}
```

**Action:** Copy the `token` value from the response - you'll need it for subsequent requests.

---

### Step 2: Login with Existing User

**Request:**
- **Method:** `POST`
- **URL:** `http://localhost:8082/api/auth/login`
- **Headers:**
  ```
  Content-Type: application/json
  ```
- **Body (raw JSON):**
  ```json
  {
    "email": "testuser@example.com",
    "password": "password123"
  }
  ```

**Expected Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "user": {
    "id": 1,
    "email": "testuser@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "type": "CLIENT"
  }
}
```

**Action:** Copy the `token` value from the response.

---

### Step 3: Validate JWT Token

**Request:**
- **Method:** `GET`
- **URL:** `http://localhost:8082/api/auth/validate`
- **Headers:**
  ```
  Authorization: Bearer <your-token-here>
  ```
  Replace `<your-token-here>` with the token from Step 1 or 2.

**Expected Response (200 OK):**
```json
{
  "valid": true,
  "user": {
    "id": 1,
    "email": "testuser@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "type": "CLIENT"
  }
}
```

---

### Step 4: Access Protected Endpoint (Get All Places)

**Request:**
- **Method:** `GET`
- **URL:** `http://localhost:8082/api/place/all`
- **Headers:**
  ```
  Authorization: Bearer <your-token-here>
  ```
  Replace `<your-token-here>` with the token from Step 1 or 2.

**Expected Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Place Name",
    ...
  }
]
```

**Test Without Token:**
- Try the same request **without** the Authorization header
- **Expected Response:** `401 Unauthorized`

---

### Step 5: Access Protected Endpoint (Get All Users)

**Request:**
- **Method:** `GET`
- **URL:** `http://localhost:8082/api/user/all`
- **Headers:**
  ```
  Authorization: Bearer <your-token-here>
  ```

**Expected Response (200 OK):**
```json
[
  {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "testuser@example.com",
    "phoneNumber": "+1234567890",
    "type": "CLIENT"
  }
]
```

---

### Step 6: Create a Place (Protected Endpoint)

**Request:**
- **Method:** `POST`
- **URL:** `http://localhost:8082/api/place/save`
- **Headers:**
  ```
  Content-Type: application/json
  Authorization: Bearer <your-token-here>
  ```
- **Body (raw JSON):**
  ```json
  {
    "name": "Test Place",
    "description": "A test place",
    "location": "Test Location",
    "category": "TOURIST_ATTRACTION"
  }
  ```

**Expected Response (201 Created):**
```json
{
  "id": 1,
  "name": "Test Place",
  "description": "A test place",
  "location": "Test Location",
  "category": "TOURIST_ATTRACTION"
}
```

---

## Testing Scenarios

### Scenario 1: Invalid Token
**Request:**
- **Method:** `GET`
- **URL:** `http://localhost:8082/api/place/all`
- **Headers:**
  ```
  Authorization: Bearer invalid_token_here
  ```
- **Expected Response:** `401 Unauthorized` or `403 Forbidden`

### Scenario 2: Missing Token
**Request:**
- **Method:** `GET`
- **URL:** `http://localhost:8082/api/place/all`
- **Headers:** (No Authorization header)
- **Expected Response:** `401 Unauthorized`

### Scenario 3: Expired Token
1. Wait 24 hours (or modify token expiration in `application.properties`)
2. Try to access a protected endpoint
3. **Expected Response:** `401 Unauthorized`

### Scenario 4: Wrong Password
**Request:**
- **Method:** `POST`
- **URL:** `http://localhost:8082/api/auth/login`
- **Body:**
  ```json
  {
    "email": "testuser@example.com",
    "password": "wrongpassword"
  }
  ```
- **Expected Response:** `401 Unauthorized`

### Scenario 5: Register with Existing Email
**Request:**
- **Method:** `POST`
- **URL:** `http://localhost:8082/api/auth/register`
- **Body:**
  ```json
  {
    "email": "testuser@example.com",
    "password": "password123",
    "firstName": "Jane",
    "lastName": "Doe",
    "type": "CLIENT"
  }
  ```
- **Expected Response:** `400 Bad Request` with error message

---

## Postman Collection Variables

To make testing easier, set up these variables in Postman:

1. **base_url:** `http://localhost:8082`
2. **jwt_token:** (will be set automatically via script)

### Postman Pre-request Script (for Login/Register)

Add this script to automatically save the token:

```javascript
// This script runs after the request
// Add it in the "Tests" tab of the Login/Register requests
if (pm.response.code === 200 || pm.response.code === 201) {
    var jsonData = pm.response.json();
    if (jsonData.token) {
        pm.environment.set("jwt_token", jsonData.token);
        console.log("Token saved:", jsonData.token);
    }
}
```

### Using Variables in Requests

In your Authorization header, use:
```
Bearer {{jwt_token}}
```

---

## Quick Test Checklist

- [ ] Register a new user → Get token
- [ ] Login with credentials → Get token
- [ ] Validate token → Should return user info
- [ ] Access protected endpoint with token → Should work
- [ ] Access protected endpoint without token → Should fail (401)
- [ ] Access protected endpoint with invalid token → Should fail (401)
- [ ] Create resource with token → Should work
- [ ] Login with wrong password → Should fail (401)

---

## Common Issues

### Issue: 401 Unauthorized even with valid token
**Solution:** 
- Check that the Authorization header format is exactly: `Bearer <token>` (with a space)
- Ensure the token hasn't expired
- Verify the token was copied completely (no truncation)

### Issue: 403 Forbidden
**Solution:**
- Check user permissions/roles
- Verify the endpoint requires authentication

### Issue: Token not working after server restart
**Solution:**
- This is normal - tokens are stateless but server restart may clear sessions
- Just login again to get a new token

---

## Example Complete Test Flow

1. **Register:** `POST /api/auth/register` → Save token
2. **Validate:** `GET /api/auth/validate` (with token) → Should return user
3. **Get Places:** `GET /api/place/all` (with token) → Should return places
4. **Create Place:** `POST /api/place/save` (with token) → Should create place
5. **Get Users:** `GET /api/user/all` (with token) → Should return users
6. **Test Without Token:** `GET /api/place/all` (no token) → Should return 401

