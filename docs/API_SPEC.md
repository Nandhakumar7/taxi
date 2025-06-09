# Taxi Booking Application - API Specification

## Base URL
```
https://api.taxiapp.com/v1
```

## Global Headers
```
Accept: application/json
Content-Type: application/json
Authorization: Bearer <JWT_TOKEN>
```

## Global Error Response Structure
```json
{
  "status": "error",
  "code": "ERROR_CODE",
  "message": "Human readable message",
  "timestamp": "2024-03-21T10:00:00Z",
  "path": "/api/v1/endpoint",
  "details": {} // Additional error details if any
}
```

## Authentication Module

### Register User
- **Endpoint**: `POST /auth/register`
- **Description**: Register a new user (passenger or driver)
- **Authentication**: None
- **Request Body**:
```json
{
  "email": "string (required)",
  "password": "string (required, min: 8)",
  "phoneNumber": "string (required)",
  "fullName": "string (required)",
  "userType": "enum (required) [passenger, driver]"
}
```
- **Success Response** (201 Created):
```json
{
  "status": "success",
  "data": {
    "userId": "uuid",
    "email": "string",
    "fullName": "string",
    "userType": "string",
    "token": "JWT_TOKEN"
  }
}
```
- **Error Responses**:
  - 400: INVALID_REQUEST - Invalid input data
  - 409: EMAIL_EXISTS - Email already registered
  - 409: PHONE_EXISTS - Phone number already registered

### Login
- **Endpoint**: `POST /auth/login`
- **Description**: Authenticate user and get JWT token
- **Authentication**: None
- **Request Body**:
```json
{
  "email": "string (required)",
  "password": "string (required)"
}
```
- **Success Response** (200 OK):
```json
{
  "status": "success",
  "data": {
    "token": "JWT_TOKEN",
    "refreshToken": "REFRESH_TOKEN",
    "expiresIn": 3600
  }
}
```
- **Error Responses**:
  - 400: INVALID_CREDENTIALS - Invalid email or password
  - 403: ACCOUNT_SUSPENDED - Account is suspended

## User Profile Module

### Get User Profile
- **Endpoint**: `GET /users/profile`
- **Description**: Get current user's profile
- **Authentication**: JWT Required
- **Success Response** (200 OK):
```json
{
  "status": "success",
  "data": {
    "userId": "uuid",
    "email": "string",
    "fullName": "string",
    "phoneNumber": "string",
    "userType": "string",
    "profile": {
      // Passenger specific fields
      "homeAddress": "string",
      "workAddress": "string",
      "averageRating": "number",
      
      // Driver specific fields
      "licenseNumber": "string",
      "vehicleDetails": {
        "plate": "string",
        "model": "string",
        "year": "number"
      },
      "isAvailable": "boolean",
      "averageRating": "number"
    }
  }
}
```

### Update User Profile
- **Endpoint**: `PUT /users/profile`
- **Description**: Update user profile information
- **Authentication**: JWT Required
- **Request Body**: (Fields are optional unless specified)
```json
{
  "fullName": "string",
  "homeAddress": "string",
  "workAddress": "string",
  "vehicleDetails": {
    "model": "string",
    "year": "number"
  }
}
```
- **Success Response** (200 OK):
```json
{
  "status": "success",
  "data": {
    // Updated profile data
  }
}
```

## Driver Module

### Update Location
- **Endpoint**: `PUT /drivers/location`
- **Description**: Update driver's current location
- **Authentication**: JWT Required (Driver Only)
- **Request Body**:
```json
{
  "latitude": "number (required)",
  "longitude": "number (required)"
}
```
- **Success Response** (200 OK):
```json
{
  "status": "success",
  "data": {
    "latitude": "number",
    "longitude": "number",
    "timestamp": "string"
  }
}
```

### Update Availability
- **Endpoint**: `PUT /drivers/availability`
- **Description**: Update driver's availability status
- **Authentication**: JWT Required (Driver Only)
- **Request Body**:
```json
{
  "isAvailable": "boolean (required)"
}
```
- **Success Response** (200 OK):
```json
{
  "status": "success",
  "data": {
    "isAvailable": "boolean",
    "timestamp": "string"
  }
}
```

### Get Earnings
- **Endpoint**: `GET /drivers/earnings`
- **Description**: Get driver's earnings
- **Authentication**: JWT Required (Driver Only)
- **Query Parameters**:
  - `startDate`: string (ISO date)
  - `endDate`: string (ISO date)
  - `page`: integer (default: 1)
  - `limit`: integer (default: 20)
- **Success Response** (200 OK):
```json
{
  "status": "success",
  "data": {
    "summary": {
      "totalEarnings": "number",
      "totalRides": "number",
      "totalHours": "number"
    },
    "earnings": [
      {
        "rideId": "uuid",
        "amount": "number",
        "commission": "number",
        "netAmount": "number",
        "date": "string",
        "status": "string"
      }
    ],
    "pagination": {
      "page": "number",
      "limit": "number",
      "total": "number",
      "pages": "number"
    }
  }
}
```

## Booking Module

### Create Booking
- **Endpoint**: `POST /bookings`
- **Description**: Create a new ride booking
- **Authentication**: JWT Required (Passenger Only)
- **Request Body**:
```json
{
  "pickup": {
    "latitude": "number (required)",
    "longitude": "number (required)",
    "address": "string (required)"
  },
  "dropoff": {
    "latitude": "number (required)",
    "longitude": "number (required)",
    "address": "string (required)"
  },
  "scheduledTime": "string (ISO datetime, optional)",
  "paymentMethodId": "uuid (required)"
}
```
- **Success Response** (201 Created):
```json
{
  "status": "success",
  "data": {
    "bookingId": "uuid",
    "status": "string",
    "estimatedFare": "number",
    "estimatedDuration": "number",
    "estimatedDistance": "number"
  }
}
```
- **Error Responses**:
  - 400: INVALID_LOCATION - Invalid pickup/dropoff location
  - 400: INVALID_SCHEDULE_TIME - Invalid scheduled time
  - 404: PAYMENT_METHOD_NOT_FOUND - Payment method not found
  - 412: NO_DRIVERS_AVAILABLE - No drivers available

### Get Booking Status
- **Endpoint**: `GET /bookings/{bookingId}`
- **Description**: Get booking details and status
- **Authentication**: JWT Required
- **Path Parameters**:
  - `bookingId`: UUID of the booking
- **Success Response** (200 OK):
```json
{
  "status": "success",
  "data": {
    "bookingId": "uuid",
    "status": "string",
    "pickup": {
      "latitude": "number",
      "longitude": "number",
      "address": "string"
    },
    "dropoff": {
      "latitude": "number",
      "longitude": "number",
      "address": "string"
    },
    "driver": {
      "id": "uuid",
      "name": "string",
      "phoneNumber": "string",
      "vehicleDetails": {
        "plate": "string",
        "model": "string"
      },
      "currentLocation": {
        "latitude": "number",
        "longitude": "number"
      }
    },
    "fare": {
      "base": "number",
      "final": "number"
    },
    "timestamps": {
      "created": "string",
      "accepted": "string",
      "started": "string",
      "completed": "string"
    }
  }
}
```

### Cancel Booking
- **Endpoint**: `POST /bookings/{bookingId}/cancel`
- **Description**: Cancel an existing booking
- **Authentication**: JWT Required
- **Path Parameters**:
  - `bookingId`: UUID of the booking
- **Request Body**:
```json
{
  "reason": "string (required)",
  "details": "string (optional)"
}
```
- **Success Response** (200 OK):
```json
{
  "status": "success",
  "data": {
    "bookingId": "uuid",
    "status": "cancelled",
    "cancellationFee": "number",
    "timestamp": "string"
  }
}
```
- **Error Responses**:
  - 404: BOOKING_NOT_FOUND - Booking not found
  - 400: INVALID_STATE - Booking cannot be cancelled in current state

## Payment Module

### Get Payment Methods
- **Endpoint**: `GET /payments/methods`
- **Description**: Get user's saved payment methods
- **Authentication**: JWT Required
- **Success Response** (200 OK):
```json
{
  "status": "success",
  "data": {
    "methods": [
      {
        "id": "uuid",
        "type": "string",
        "provider": "string",
        "lastFour": "string",
        "isDefault": "boolean",
        "expiryDate": "string"
      }
    ]
  }
}
```

### Add Payment Method
- **Endpoint**: `POST /payments/methods`
- **Description**: Add a new payment method
- **Authentication**: JWT Required
- **Request Body**:
```json
{
  "type": "string (required)",
  "token": "string (required)", // Payment provider token
  "isDefault": "boolean (optional)"
}
```
- **Success Response** (201 Created):
```json
{
  "status": "success",
  "data": {
    "id": "uuid",
    "type": "string",
    "lastFour": "string",
    "isDefault": "boolean"
  }
}
```

## Rating Module

### Submit Rating
- **Endpoint**: `POST /ratings`
- **Description**: Submit rating for a completed ride
- **Authentication**: JWT Required
- **Request Body**:
```json
{
  "rideId": "uuid (required)",
  "rating": "number (required, 1-5)",
  "comment": "string (optional)"
}
```
- **Success Response** (201 Created):
```json
{
  "status": "success",
  "data": {
    "id": "uuid",
    "rating": "number",
    "timestamp": "string"
  }
}
```
- **Error Responses**:
  - 404: RIDE_NOT_FOUND - Ride not found
  - 400: INVALID_RATING - Rating must be between 1 and 5
  - 409: ALREADY_RATED - Ride already rated

## Security Considerations

### Rate Limiting
- Authentication endpoints: 5 requests per minute per IP
- Profile updates: 10 requests per minute per user
- Booking creation: 2 requests per minute per user
- Location updates: 60 requests per minute per driver

### Input Validation
- All coordinates must be valid latitude/longitude values
- Phone numbers must match E.164 format
- Emails must be valid format
- Passwords must meet complexity requirements
- All IDs must be valid UUIDs

### Authorization
- JWT tokens expire after 1 hour
- Refresh tokens expire after 30 days
- Role-based access control for driver/passenger specific endpoints
- Resource ownership validation for all protected endpoints

### API Versioning
- Version included in URL path (/v1)
- Support for multiple versions simultaneously
- Deprecation notices via response headers

### Monitoring & Logging
- Request logging with correlation IDs
- Error tracking with stack traces
- Performance monitoring
- Rate limit tracking
- Security event logging 