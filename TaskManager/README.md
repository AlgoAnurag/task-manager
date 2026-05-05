# Team Task Manager - Full Stack Application

A comprehensive web application for managing team projects and tasks with role-based access control. Built with Spring Boot (backend) and React (frontend).

## 🚀 Features

### Authentication & Authorization
- User signup and login with JWT authentication
- Role-based access control (Admin/Member)
- Secure password hashing with BCrypt
- Token-based session management

### Project Management
- Create, read, update, and delete projects
- Add/remove team members to projects
- Project status tracking (ACTIVE, ARCHIVED, COMPLETED)
- Real-time project visibility

### Task Management
- Create tasks within projects
- Assign tasks to team members
- Task status tracking (TODO, IN_PROGRESS, IN_REVIEW, COMPLETED, CANCELLED)
- Priority levels (LOW, MEDIUM, HIGH, URGENT)
- Due date tracking
- Overdue task detection

### Dashboard
- Overview of all projects and tasks
- Task statistics (total, completed, in progress, overdue)
- Recent projects and tasks
- Quick task access

## 📋 Tech Stack

### Backend
- **Spring Boot 4.0.6** - Web framework
- **Spring Data JPA** - Database access
- **Spring Security** - Authentication & authorization
- **JWT (jjwt 0.11.5)** - Token-based authentication
- **PostgreSQL** - Production database
- **H2 Database** - Testing database
- **Maven** - Build tool

### Frontend
- **React 18.2** - UI library
- **React Router 6** - Navigation
- **Axios** - HTTP client
- **Tailwind CSS** - Styling
- **Vite** - Build tool

## 🛠️ Installation & Setup

### Prerequisites
- Java 17+
- Node.js 16+
- PostgreSQL 12+
- Maven 3.6+

### Backend Setup

1. **Clone the repository**
```bash
git clone <repository-url>
cd TaskManager
```

2. **Set up PostgreSQL database**
```bash
# Create database
createdb taskmanager

# Create user (optional)
psql -U postgres
CREATE USER taskmanager WITH PASSWORD 'postgres';
GRANT ALL PRIVILEGES ON DATABASE taskmanager TO taskmanager;
```

3. **Configure application.properties**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/taskmanager
spring.datasource.username=postgres
spring.datasource.password=postgres
app.jwt.secret=your-secret-key-here
```

4. **Build and run**
```bash
./mvnw clean install
./mvnw spring-boot:run
```

The backend will be available at `http://localhost:8080/api/v1`

### Frontend Setup

1. **Navigate to frontend directory**
```bash
cd frontend
```

2. **Install dependencies**
```bash
npm install
```

3. **Create .env file**
```bash
REACT_APP_API_URL=http://localhost:8080/api/v1
```

4. **Start development server**
```bash
npm run dev
```

The frontend will be available at `http://localhost:5173`

## 📚 API Endpoints

### Authentication
- `POST /auth/signup` - Register new user
- `POST /auth/login` - User login
- `GET /auth/health` - Health check

### Projects
- `POST /projects` - Create project
- `GET /projects` - Get all user projects
- `GET /projects/{id}` - Get project details
- `PUT /projects/{id}` - Update project
- `DELETE /projects/{id}` - Delete project
- `POST /projects/{id}/members/{memberId}` - Add member
- `DELETE /projects/{id}/members/{memberId}` - Remove member

### Tasks
- `POST /tasks` - Create task
- `GET /tasks/{id}` - Get task
- `GET /tasks/project/{projectId}` - Get project tasks
- `GET /tasks/assigned/me` - Get assigned tasks
- `GET /tasks/overdue/me` - Get overdue tasks
- `PUT /tasks/{id}` - Update task
- `DELETE /tasks/{id}` - Delete task

### Dashboard
- `GET /dashboard` - Get dashboard statistics

## 🔐 Security Features

- JWT-based authentication
- CORS configuration
- Password hashing with BCrypt
- Role-based access control
- Request validation
- Secure error handling

## 📦 Database Schema

### Users Table
- id (PK)
- email (unique)
- password (hashed)
- firstName, lastName
- role (ADMIN/MEMBER)
- isActive
- createdAt, updatedAt

### Projects Table
- id (PK)
- name, description
- createdBy (FK to Users)
- status (ACTIVE/ARCHIVED/COMPLETED)
- createdAt, updatedAt
- Members (Many-to-Many with Users)

### Tasks Table
- id (PK)
- title, description
- project (FK to Projects)
- assignedTo (FK to Users)
- status, priority
- dueDate
- createdAt, updatedAt, completedAt

## 🚀 Deployment

### Deploy Backend to Railway

1. **Create Railway account** at https://railway.app

2. **Connect GitHub repository**
   - Link your GitHub account
   - Select this repository

3. **Configure environment variables**
   - `SPRING_DATASOURCE_URL`: PostgreSQL connection URL
   - `SPRING_DATASOURCE_USERNAME`: Database user
   - `SPRING_DATASOURCE_PASSWORD`: Database password
   - `APP_JWT_SECRET`: JWT secret key

4. **Deploy**
   - Railway will automatically detect Maven project
   - Build and deploy will start automatically

### Deploy Frontend to Vercel/Netlify

1. **Build frontend**
```bash
cd frontend
npm run build
```

2. **Deploy to Vercel**
   - Connect your GitHub repository to Vercel
   - Set build command: `npm run build`
   - Set output directory: `dist`
   - Set environment variable: `REACT_APP_API_URL=<your-railway-backend-url>`

## 📝 Usage Example

### 1. Sign Up
```bash
POST /auth/signup
{
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "password": "password123"
}
```

### 2. Login
```bash
POST /auth/login
{
  "email": "user@example.com",
  "password": "password123"
}
```

### 3. Create Project
```bash
POST /projects
Authorization: Bearer <token>
{
  "name": "Website Redesign",
  "description": "Redesign company website"
}
```

### 4. Create Task
```bash
POST /tasks
Authorization: Bearer <token>
{
  "title": "Design homepage",
  "description": "Create wireframes",
  "projectId": 1,
  "priority": "HIGH",
  "dueDate": "2024-12-31T23:59:59"
}
```

## 🔧 Configuration

### JWT Configuration
Edit `application.properties`:
```properties
app.jwt.secret=your-super-secret-key
app.jwt.expiration=86400000 # 24 hours in milliseconds
```

### CORS Configuration
```properties
app.cors.allowed-origins=http://localhost:3000,http://localhost:5173
app.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
app.cors.allowed-headers=*
```

## 📊 Project Statistics

- **Total API Endpoints**: 15+
- **Database Tables**: 4 (Users, Projects, Tasks, ProjectMembers)
- **React Components**: 10+
- **Security Features**: 5+ (JWT, CORS, BCrypt, Role-based, Validation)

## 🐛 Troubleshooting

### Backend Issues
- Ensure PostgreSQL is running
- Check database credentials in `application.properties`
- Verify Java 17+ is installed

### Frontend Issues
- Clear node_modules and reinstall: `rm -rf node_modules && npm install`
- Check if backend URL is correct in `.env`
- Verify backend is running on port 8080

### CORS Errors
- Ensure frontend URL is in `app.cors.allowed-origins`
- Check if backend is running on the correct port

## 📄 License

This project is open source and available under the MIT License.

## 👥 Contributors

- Project built as a full-stack application assignment

## 📞 Support

For issues or questions, please create an issue in the repository.

---

**Ready to deploy?** Follow the deployment instructions above to get your app live on Railway!
