# Postman Testing Guide for Admin JWT Authentication

## Overview
All CRUD operations in the REST API now require admin JWT authentication. This guide provides step-by-step instructions for testing admin login and CRUD operations using Postman.

## Default Admin Credentials
- **Login**: `admin`
- **Password**: `admin`

## Step 1: Admin Login

### Request
- **Method**: `POST`
- **URL**: `http://localhost:8082/api/auth/admin/login`
- **Headers**:
  - `Content-Type: application/json`
- **Body** (raw JSON):
```json
{
  "login": "admin",
  "password": "admin"
}
```

### Expected Response
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJ0eXBlIjoiQURNSU4iLCJzdWIiOiJhZG1pbiIsImlhdCI6MTYz...",
  "type": "Bearer",
  "admin": {
    "id": 1,
    "login": "admin"
  }
}
```

### Save the Token
Copy the `token` value from the response. You'll need to use it in the `Authorization` header for all subsequent CRUD operations.

---

## Step 2: Test CRUD Operations

### Setting Up Authorization Header
For all CRUD operations, add the following header:
- **Header Name**: `Authorization`
- **Header Value**: `Bearer <your-token-here>`

Replace `<your-token-here>` with the token you received from the admin login.

---

## Step 3: Test User CRUD Operations

### Create User (POST)
- **Method**: `POST`
- **URL**: `http://localhost:8082/api/user/save`
- **Headers**:
  - `Content-Type: application/json`
  - `Authorization: Bearer <your-token>`
- **Body** (raw JSON):
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "password123",
  "phoneNumber": "+1234567890",
  "type": "CLIENT"
}
```

### Get All Users (GET)
- **Method**: `GET`
- **URL**: `http://localhost:8082/api/user/all`
- **Headers**:
  - `Authorization: Bearer <your-token>`

### Get User by ID (GET)
- **Method**: `GET`
- **URL**: `http://localhost:8082/api/user/getone/1`
- **Headers**:
  - `Authorization: Bearer <your-token>`

### Update User (PUT)
- **Method**: `PUT`
- **URL**: `http://localhost:8082/api/user/update/1`
- **Headers**:
  - `Content-Type: application/json`
  - `Authorization: Bearer <your-token>`
- **Body** (raw JSON):
```json
{
  "firstName": "Jane",
  "lastName": "Doe",
  "email": "jane.doe@example.com",
  "password": "newpassword123",
  "phoneNumber": "+1234567890",
  "type": "CLIENT"
}
```

### Delete User (DELETE)
- **Method**: `DELETE`
- **URL**: `http://localhost:8082/api/user/1`
- **Headers**:
  - `Authorization: Bearer <your-token>`

---

## Step 4: Test Place CRUD Operations

### Create Place (POST)
- **Method**: `POST`
- **URL**: `http://localhost:8082/api/place/save`
- **Headers**:
  - `Content-Type: application/json`
  - `Authorization: Bearer <your-token>`
- **Body** (raw JSON):
```json
{
  "name": "Eiffel Tower",
  "description": "Iconic iron tower in Paris",
  "location": "Paris, France",
  "category": "LANDMARK"
}
```

### Get All Places (GET)
- **Method**: `GET`
- **URL**: `http://localhost:8082/api/place/all`
- **Headers**:
  - `Authorization: Bearer <your-token>`

### Update Place (PUT)
- **Method**: `PUT`
- **URL**: `http://localhost:8082/api/place/update/1`
- **Headers**:
  - `Content-Type: application/json`
  - `Authorization: Bearer <your-token>`
- **Body** (raw JSON):
```json
{
  "name": "Eiffel Tower",
  "description": "Updated description",
  "location": "Paris, France",
  "category": "LANDMARK"
}
```

### Delete Place (DELETE)
- **Method**: `DELETE`
- **URL**: `http://localhost:8082/api/place/1`
- **Headers**:
  - `Authorization: Bearer <your-token>`

---

## Step 5: Test Tour CRUD Operations

### Create Tour (POST)
- **Method**: `POST`
- **URL**: `http://localhost:8082/api/tour/save`
- **Headers**:
  - `Content-Type: application/json`
  - `Authorization: Bearer <your-token>`
- **Body** (raw JSON):
```json
{
  "name": "Paris City Tour",
  "description": "Explore the beautiful city of Paris",
  "duration": 4,
  "price": 99.99
}
```

### Get All Tours (GET)
- **Method**: `GET`
- **URL**: `http://localhost:8082/api/tour/all`
- **Headers**:
  - `Authorization: Bearer <your-token>`

### Update Tour (PUT)
- **Method**: `PUT`
- **URL**: `http://localhost:8082/api/tour/update/1`
- **Headers**:
  - `Content-Type: application/json`
  - `Authorization: Bearer <your-token>`
- **Body** (raw JSON):
```json
{
  "name": "Paris City Tour - Extended",
  "description": "Extended tour of Paris",
  "duration": 6,
  "price": 149.99
}
```

### Delete Tour (DELETE)
- **Method**: `DELETE`
- **URL**: `http://localhost:8082/api/tour/1`
- **Headers**:
  - `Authorization: Bearer <your-token>`

---

## Step 6: Test Guide CRUD Operations

### Create Guide (POST)
- **Method**: `POST`
- **URL**: `http://localhost:8082/api/guide/save`
- **Headers**:
  - `Content-Type: application/json`
  - `Authorization: Bearer <your-token>`
- **Body** (raw JSON):
```json
{
  "firstName": "Alice",
  "lastName": "Smith",
  "email": "alice.smith@example.com",
  "password": "password123",
  "phoneNumber": "+1234567890",
  "type": "GUIDE",
  "specialization": "Historical Tours",
  "rating": 4.5
}
```

### Get All Guides (GET)
- **Method**: `GET`
- **URL**: `http://localhost:8082/api/guide/all`
- **Headers**:
  - `Authorization: Bearer <your-token>`

### Update Guide (PUT)
- **Method**: `PUT`
- **URL**: `http://localhost:8082/api/guide/update/1`
- **Headers**:
  - `Content-Type: application/json`
  - `Authorization: Bearer <your-token>`
- **Body** (raw JSON):
```json
{
  "firstName": "Alice",
  "lastName": "Smith",
  "email": "alice.smith@example.com",
  "password": "password123",
  "phoneNumber": "+1234567890",
  "type": "GUIDE",
  "specialization": "Cultural Tours",
  "rating": 4.8
}
```

### Delete Guide (DELETE)
- **Method**: `DELETE`
- **URL**: `http://localhost:8082/api/guide/1`
- **Headers**:
  - `Authorization: Bearer <your-token>`

---

## Step 7: Test Reservation CRUD Operations

### Create Reservation (POST)
- **Method**: `POST`
- **URL**: `http://localhost:8082/api/reservation/save`
- **Headers**:
  - `Content-Type: application/json`
  - `Authorization: Bearer <your-token>`
- **Body** (raw JSON):
```json
{
  "userId": 1,
  "tourId": 1,
  "reservationDate": "2024-12-25",
  "status": "PENDING"
}
```

### Get All Reservations (GET)
- **Method**: `GET`
- **URL**: `http://localhost:8082/api/reservation/all`
- **Headers**:
  - `Authorization: Bearer <your-token>`

### Update Reservation (PUT)
- **Method**: `PUT`
- **URL**: `http://localhost:8082/api/reservation/update/1`
- **Headers**:
  - `Content-Type: application/json`
  - `Authorization: Bearer <your-token>`
- **Body** (raw JSON):
```json
{
  "userId": 1,
  "tourId": 1,
  "reservationDate": "2024-12-25",
  "status": "CONFIRMED"
}
```

### Delete Reservation (DELETE)
- **Method**: `DELETE`
- **URL**: `http://localhost:8082/api/reservation/1`
- **Headers**:
  - `Authorization: Bearer <your-token>`

---

## Step 8: Test Review CRUD Operations

### Create Review (POST)
- **Method**: `POST`
- **URL**: `http://localhost:8082/api/review/save`
- **Headers**:
  - `Content-Type: application/json`
  - `Authorization: Bearer <your-token>`
- **Body** (raw JSON):
```json
{
  "userId": 1,
  "tourId": 1,
  "rating": 5,
  "comment": "Excellent tour!"
}
```

### Get All Reviews (GET)
- **Method**: `GET`
- **URL**: `http://localhost:8082/api/review/all`
- **Headers**:
  - `Authorization: Bearer <your-token>`

### Update Review (PUT)
- **Method**: `PUT`
- **URL**: `http://localhost:8082/api/review/update/1`
- **Headers**:
  - `Content-Type: application/json`
  - `Authorization: Bearer <your-token>`
- **Body** (raw JSON):
```json
{
  "userId": 1,
  "tourId": 1,
  "rating": 4,
  "comment": "Good tour, but could be better"
}
```

### Delete Review (DELETE)
- **Method**: `DELETE`
- **URL**: `http://localhost:8082/api/review/1`
- **Headers**:
  - `Authorization: Bearer <your-token>`

---

## Step 9: Test Favorite CRUD Operations

### Create Favorite (POST)
- **Method**: `POST`
- **URL**: `http://localhost:8082/api/favorite/save`
- **Headers**:
  - `Content-Type: application/json`
  - `Authorization: Bearer <your-token>`
- **Body** (raw JSON):
```json
{
  "userId": 1,
  "placeId": 1
}
```

### Get All Favorites (GET)
- **Method**: `GET`
- **URL**: `http://localhost:8082/api/favorite/all`
- **Headers**:
  - `Authorization: Bearer <your-token>`

### Update Favorite (PUT)
- **Method**: `PUT`
- **URL**: `http://localhost:8082/api/favorite/update/1`
- **Headers**:
  - `Content-Type: application/json`
  - `Authorization: Bearer <your-token>`
- **Body** (raw JSON):
```json
{
  "userId": 1,
  "placeId": 2
}
```

### Delete Favorite (DELETE)
- **Method**: `DELETE`
- **URL**: `http://localhost:8082/api/favorite/1`
- **Headers**:
  - `Authorization: Bearer <your-token>`

---

## Step 10: Test Admin CRUD Operations

### Create Admin (POST)
- **Method**: `POST`
- **URL**: `http://localhost:8082/api/admin/save`
- **Headers**:
  - `Content-Type: application/json`
  - `Authorization: Bearer <your-token>`
- **Body** (raw JSON):
```json
{
  "login": "admin2",
  "password": "admin123"
}
```

### Get All Admins (GET)
- **Method**: `GET`
- **URL**: `http://localhost:8082/api/admin/all`
- **Headers**:
  - `Authorization: Bearer <your-token>`

### Get Admin by ID (GET)
- **Method**: `GET`
- **URL**: `http://localhost:8082/api/admin/getone/1`
- **Headers**:
  - `Authorization: Bearer <your-token>`

### Update Admin (PUT)
- **Method**: `PUT`
- **URL**: `http://localhost:8082/api/admin/update/1`
- **Headers**:
  - `Content-Type: application/json`
  - `Authorization: Bearer <your-token>`
- **Body** (raw JSON):
```json
{
  "login": "admin",
  "password": "newpassword123"
}
```

### Delete Admin (DELETE)
- **Method**: `DELETE`
- **URL**: `http://localhost:8082/api/admin/1`
- **Headers**:
  - `Authorization: Bearer <your-token>`

---

## Testing Without Admin Token (Should Fail)

Try making any CRUD request without the `Authorization` header or with an invalid token:

### Example: Get All Users Without Token
- **Method**: `GET`
- **URL**: `http://localhost:8082/api/user/all`
- **Headers**: (none)

### Expected Response
- **Status Code**: `403 Forbidden`
- **Body**: (empty)

---

## Testing with Regular User Token (Should Fail)

1. First, login as a regular user:
   - **Method**: `POST`
   - **URL**: `http://localhost:8082/api/auth/login`
   - **Body**:
   ```json
   {
     "email": "user@example.com",
     "password": "password123"
   }
   ```

2. Try to use that token for a CRUD operation:
   - **Method**: `GET`
   - **URL**: `http://localhost:8082/api/user/all`
   - **Headers**:
     - `Authorization: Bearer <regular-user-token>`

### Expected Response
- **Status Code**: `403 Forbidden`
- Only admin tokens are accepted for CRUD operations.

---

## Postman Collection Variables

To make testing easier, you can set up Postman environment variables:

1. Create a new environment in Postman
2. Add variables:
   - `base_url`: `http://localhost:8082`
   - `admin_token`: (will be set after login)

3. After admin login, use a test script to save the token:
```javascript
if (pm.response.code === 200) {
    var jsonData = pm.response.json();
    pm.environment.set("admin_token", jsonData.token);
}
```

4. Then use `{{admin_token}}` in your Authorization header:
   - `Authorization: Bearer {{admin_token}}`

---

## Summary

- ✅ Admin login endpoint: `/api/auth/admin/login`
- ✅ All CRUD operations require admin JWT token in `Authorization` header
- ✅ Token format: `Bearer <token>`
- ✅ Without admin token: Returns `403 Forbidden`
- ✅ Regular user tokens: Also return `403 Forbidden` for CRUD operations

