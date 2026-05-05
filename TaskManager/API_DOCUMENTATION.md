# API Documentation - Team Task Manager

Complete API reference for the Team Task Manager backend.

## Base URL

```
http://localhost:8080/api/v1
```

For production:
```
https://your-railway-backend-url/api/v1
```

## Authentication

All endpoints (except `/auth/signup` and `/auth/login`) require JWT token in the `Authorization` header:

```bash
Authorization: Bearer YOUR_JWT_TOKEN
```

---

## 📋 Authentication Endpoints

### 1. Sign Up

**Endpoint:** `POST /auth/signup`

**Public:** ✅ Yes (no authentication required)

**Request Body:**
```json
{
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "password": "password123"
}
```

**Validations:**
- email: valid email format, unique in database
- firstName: min 2 characters
- lastName: min 2 characters
- password: min 6 characters

**Response (201 Created):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "id": 1,
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "role": "MEMBER"
}
```

**Error Response (400 Bad Request):**
```json
{
  "error": "Email already exists"
}
```

---

### 2. Login

**Endpoint:** `POST /auth/login`

**Public:** ✅ Yes (no authentication required)

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "id": 1,
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "role": "MEMBER"
}
```

**Error Response (401 Unauthorized):**
```json
{
  "error": "Invalid email or password"
}
```

---

### 3. Health Check

**Endpoint:** `GET /auth/health`

**Public:** ✅ Yes (no authentication required)

**Response (200 OK):**
```
Server is running
```

---

## 📁 Project Endpoints

### 1. Create Project

**Endpoint:** `POST /projects`

**Authentication:** ✅ Required

**Request Body:**
```json
{
  "name": "Website Redesign",
  "description": "Redesign company website"
}
```

**Validations:**
- name: required, not blank
- description: optional

**Response (201 Created):**
```json
{
  "id": 1,
  "name": "Website Redesign",
  "description": "Redesign company website",
  "createdBy": {
    "id": 1,
    "email": "user@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "role": "MEMBER"
  },
  "members": [
    {
      "id": 1,
      "email": "user@example.com",
      "firstName": "John",
      "lastName": "Doe"
    }
  ],
  "tasks": [],
  "status": "ACTIVE",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

---

### 2. Get All Projects

**Endpoint:** `GET /projects`

**Authentication:** ✅ Required

**Query Parameters:** None

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Website Redesign",
    "description": "Redesign company website",
    "status": "ACTIVE",
    "members": [],
    "tasks": []
  },
  {
    "id": 2,
    "name": "Mobile App",
    "description": "Build mobile app",
    "status": "ACTIVE",
    "members": [],
    "tasks": []
  }
]
```

---

### 3. Get Project by ID

**Endpoint:** `GET /projects/{projectId}`

**Authentication:** ✅ Required

**Path Parameters:**
- `projectId` (Long): Project ID

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "Website Redesign",
  "description": "Redesign company website",
  "createdBy": {
    "id": 1,
    "email": "user@example.com",
    "firstName": "John",
    "lastName": "Doe"
  },
  "members": [
    {
      "id": 1,
      "email": "user@example.com",
      "firstName": "John",
      "lastName": "Doe"
    }
  ],
  "tasks": [],
  "status": "ACTIVE",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

**Error Response (404 Not Found):**
```json
{
  "error": "Project not found"
}
```

---

### 4. Update Project

**Endpoint:** `PUT /projects/{projectId}`

**Authentication:** ✅ Required

**Authorization:** Project creator only

**Path Parameters:**
- `projectId` (Long): Project ID

**Request Body:**
```json
{
  "name": "Updated Project Name",
  "description": "Updated description"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "Updated Project Name",
  "description": "Updated description",
  "status": "ACTIVE",
  "updatedAt": "2024-01-15T11:00:00"
}
```

---

### 5. Delete Project

**Endpoint:** `DELETE /projects/{projectId}`

**Authentication:** ✅ Required

**Authorization:** Project creator only

**Path Parameters:**
- `projectId` (Long): Project ID

**Response (204 No Content)**

---

### 6. Add Member to Project

**Endpoint:** `POST /projects/{projectId}/members/{memberId}`

**Authentication:** ✅ Required

**Authorization:** Project creator only

**Path Parameters:**
- `projectId` (Long): Project ID
- `memberId` (Long): User ID to add

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "Website Redesign",
  "members": [
    {
      "id": 1,
      "email": "user@example.com",
      "firstName": "John",
      "lastName": "Doe"
    },
    {
      "id": 2,
      "email": "member@example.com",
      "firstName": "Jane",
      "lastName": "Smith"
    }
  ]
}
```

---

### 7. Remove Member from Project

**Endpoint:** `DELETE /projects/{projectId}/members/{memberId}`

**Authentication:** ✅ Required

**Authorization:** Project creator only

**Path Parameters:**
- `projectId` (Long): Project ID
- `memberId` (Long): User ID to remove

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "Website Redesign",
  "members": [
    {
      "id": 1,
      "email": "user@example.com",
      "firstName": "John",
      "lastName": "Doe"
    }
  ]
}
```

---

## 📝 Task Endpoints

### 1. Create Task

**Endpoint:** `POST /tasks`

**Authentication:** ✅ Required

**Request Body:**
```json
{
  "title": "Design Homepage",
  "description": "Create wireframes and mockups",
  "projectId": 1,
  "assignedToId": 2,
  "priority": "HIGH",
  "dueDate": "2024-02-15T23:59:59"
}
```

**Validations:**
- title: required, not blank
- projectId: required, project must exist
- assignedToId: optional, user must exist
- priority: LOW, MEDIUM, HIGH, URGENT
- dueDate: optional, ISO 8601 format

**Response (201 Created):**
```json
{
  "id": 1,
  "title": "Design Homepage",
  "description": "Create wireframes and mockups",
  "projectId": 1,
  "assignedTo": {
    "id": 2,
    "email": "member@example.com",
    "firstName": "Jane",
    "lastName": "Smith"
  },
  "status": "TODO",
  "priority": "HIGH",
  "dueDate": "2024-02-15T23:59:59",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

---

### 2. Get Task by ID

**Endpoint:** `GET /tasks/{taskId}`

**Authentication:** ✅ Required

**Path Parameters:**
- `taskId` (Long): Task ID

**Response (200 OK):**
```json
{
  "id": 1,
  "title": "Design Homepage",
  "description": "Create wireframes and mockups",
  "projectId": 1,
  "assignedTo": {
    "id": 2,
    "email": "member@example.com",
    "firstName": "Jane",
    "lastName": "Smith"
  },
  "status": "IN_PROGRESS",
  "priority": "HIGH",
  "dueDate": "2024-02-15T23:59:59",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T11:00:00",
  "completedAt": null
}
```

---

### 3. Get Project Tasks

**Endpoint:** `GET /tasks/project/{projectId}`

**Authentication:** ✅ Required

**Path Parameters:**
- `projectId` (Long): Project ID

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "title": "Design Homepage",
    "status": "IN_PROGRESS",
    "priority": "HIGH"
  },
  {
    "id": 2,
    "title": "Setup Database",
    "status": "COMPLETED",
    "priority": "HIGH"
  }
]
```

---

### 4. Get Assigned Tasks

**Endpoint:** `GET /tasks/assigned/me`

**Authentication:** ✅ Required

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "title": "Design Homepage",
    "projectId": 1,
    "status": "IN_PROGRESS",
    "priority": "HIGH",
    "dueDate": "2024-02-15T23:59:59"
  },
  {
    "id": 3,
    "title": "Code Review",
    "projectId": 2,
    "status": "TODO",
    "priority": "MEDIUM",
    "dueDate": "2024-02-20T23:59:59"
  }
]
```

---

### 5. Get Overdue Tasks

**Endpoint:** `GET /tasks/overdue/me`

**Authentication:** ✅ Required

**Response (200 OK):**
```json
[
  {
    "id": 5,
    "title": "Fix Bug in Login",
    "projectId": 1,
    "status": "IN_PROGRESS",
    "priority": "URGENT",
    "dueDate": "2024-01-10T23:59:59"
  }
]
```

---

### 6. Update Task

**Endpoint:** `PUT /tasks/{taskId}`

**Authentication:** ✅ Required

**Path Parameters:**
- `taskId` (Long): Task ID

**Request Body:**
```json
{
  "title": "Design Homepage",
  "description": "Updated description",
  "status": "IN_PROGRESS",
  "priority": "URGENT",
  "assignedToId": 3,
  "dueDate": "2024-02-20T23:59:59"
}
```

**Allowed Status Values:**
- TODO
- IN_PROGRESS
- IN_REVIEW
- COMPLETED
- CANCELLED

**Response (200 OK):**
```json
{
  "id": 1,
  "title": "Design Homepage",
  "status": "COMPLETED",
  "priority": "URGENT",
  "updatedAt": "2024-01-15T12:00:00",
  "completedAt": "2024-01-15T12:00:00"
}
```

---

### 7. Delete Task

**Endpoint:** `DELETE /tasks/{taskId}`

**Authentication:** ✅ Required

**Path Parameters:**
- `taskId` (Long): Task ID

**Response (204 No Content)**

---

## 📊 Dashboard Endpoints

### 1. Get Dashboard

**Endpoint:** `GET /dashboard`

**Authentication:** ✅ Required

**Response (200 OK):**
```json
{
  "totalProjects": 5,
  "totalTasks": 20,
  "completedTasks": 8,
  "tasksInProgress": 5,
  "overdueTasks": 2,
  "recentTasks": [
    {
      "id": 1,
      "title": "Recent Task 1",
      "status": "IN_PROGRESS",
      "priority": "HIGH"
    },
    {
      "id": 2,
      "title": "Recent Task 2",
      "status": "TODO",
      "priority": "MEDIUM"
    }
  ],
  "recentProjects": [
    {
      "id": 1,
      "name": "Website Redesign",
      "description": "Redesign company website",
      "members": [
        {
          "id": 1,
          "firstName": "John",
          "lastName": "Doe"
        }
      ]
    }
  ]
}
```

---

## 🔐 Error Responses

### 400 Bad Request
```json
{
  "error": "Validation failed",
  "message": "Field validation error"
}
```

### 401 Unauthorized
```json
{
  "error": "Invalid or expired token"
}
```

### 403 Forbidden
```json
{
  "error": "Access denied - insufficient permissions"
}
```

### 404 Not Found
```json
{
  "error": "Resource not found"
}
```

### 500 Internal Server Error
```json
{
  "error": "Internal server error",
  "message": "Error details"
}
```

---

## 📊 Enums Reference

### UserRole
- ADMIN
- MEMBER

### ProjectStatus
- ACTIVE
- ARCHIVED
- COMPLETED

### TaskStatus
- TODO
- IN_PROGRESS
- IN_REVIEW
- COMPLETED
- CANCELLED

### TaskPriority
- LOW
- MEDIUM
- HIGH
- URGENT

---

## 🔄 Common API Flows

### User Registration & Login
```
1. POST /auth/signup → Get token
2. Save token in localStorage
3. Use token in all subsequent requests
```

### Create Project with Tasks
```
1. POST /projects → Get projectId
2. POST /tasks (with projectId)
3. POST /projects/{projectId}/members/{memberId} → Add team members
```

### Update Task Status
```
1. GET /tasks/{taskId} → Get current task data
2. PUT /tasks/{taskId} → Update status
3. GET /dashboard → See updated statistics
```

---

## 📱 Rate Limiting

Currently no rate limiting is implemented. Consider adding for production.

---

## 🔍 Testing the API

### Using cURL

**Signup:**
```bash
curl -X POST http://localhost:8080/api/v1/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "password": "password123"
  }'
```

**Create Project:**
```bash
TOKEN="your_jwt_token_here"
curl -X POST http://localhost:8080/api/v1/projects \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Project",
    "description": "Test Description"
  }'
```

### Using Postman

1. Import endpoints from this documentation
2. Set `Authorization` header with Bearer token
3. Test endpoints with sample data

---

## 📝 API Response Examples

All responses follow this pattern:

**Success (2xx):**
```json
{
  "data": {...},
  "status": "success"
}
```

**Error (4xx, 5xx):**
```json
{
  "error": "Error message",
  "status": "error"
}
```

---

## 🚀 API Versioning

Current API version: **v1** (in URL: `/api/v1`)

Future versions will be available as `/api/v2`, etc.

---

Need more help? Check the `README.md` or `DEVELOPMENT.md` files.
