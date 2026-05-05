# Quick Start Guide - Team Task Manager

Get the Team Task Manager running in minutes!

## 🚀 Fastest Way (Docker)

### Prerequisites
- Docker & Docker Compose installed
- Terminal/Command Prompt

### Start Everything

```bash
cd /Users/KXT87690/Downloads/TaskManager

# Start all services
docker-compose up

# Open in browser
# Backend: http://localhost:8080/api/v1
# Frontend: http://localhost:5173
```

That's it! Stop with `Ctrl+C`.

---

## 🛠️ Manual Setup

### Part 1: Backend (Java + Spring Boot)

**1. Prerequisites**
```bash
# Check Java version (needs 17+)
java -version

# Check Maven
mvn -version
```

**2. Start PostgreSQL**
```bash
# Using Homebrew
brew services start postgresql

# Or Docker
docker run -d -p 5432:5432 \
  -e POSTGRES_DB=taskmanager \
  -e POSTGRES_PASSWORD=postgres \
  postgres:15-alpine
```

**3. Run Backend**
```bash
cd /Users/KXT87690/Downloads/TaskManager
./mvnw spring-boot:run

# Wait for: "Started TaskManagerApplication in X seconds"
# Backend is ready at: http://localhost:8080/api/v1
```

### Part 2: Frontend (React + Vite)

**In a new terminal:**

```bash
cd /Users/KXT87690/Downloads/TaskManager/frontend

# Install dependencies
npm install

# Start development server
npm run dev

# Open in browser: http://localhost:5173
```

---

## 🧪 Test It Out

### 1. Sign Up
- Open http://localhost:5173
- Click "Sign Up"
- Fill in the form
- Click "Sign Up"

### 2. Create Project
- Click "Projects" in navbar
- Click "+ New Project"
- Enter project details
- Click "Create Project"

### 3. Add Tasks
- Click on your project
- Click "+ Add Task"
- Enter task details
- Click "Create Task"
- Change task status by clicking the dropdown

### 4. View Dashboard
- Click "Dashboard"
- See overview of all your tasks and projects

---

## 📝 API Testing (Optional)

### Using cURL

**Sign Up:**
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

**Login:**
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

Save the `token` from response, then use it:

**Create Project:**
```bash
curl -X POST http://localhost:8080/api/v1/projects \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "My Project",
    "description": "Test project"
  }'
```

---

## 🛑 Stopping Services

### Docker
```bash
docker-compose down
```

### Manual Processes
```bash
# Stop backend
# Press Ctrl+C in the backend terminal

# Stop frontend
# Press Ctrl+C in the frontend terminal

# Stop PostgreSQL (if using Homebrew)
brew services stop postgresql

# Stop PostgreSQL (if using Docker)
docker stop taskmanager-db
```

---

## 🔧 Troubleshooting

### "Port 8080 already in use"
```bash
# Find process using port 8080
lsof -i :8080

# Kill it
kill -9 <PID>
```

### "Cannot connect to database"
```bash
# Make sure PostgreSQL is running
brew services list
# or
docker ps
```

### "Frontend can't reach backend"
```bash
# Check that backend is running on port 8080
curl http://localhost:8080/api/v1/auth/health

# Should return: "Server is running"
```

### "Port 5173 already in use"
```bash
# Kill the process
lsof -i :5173
kill -9 <PID>

# Or use different port
npm run dev -- --port 3000
```

---

## 📚 Next Steps

1. **Read Full Documentation**
   - See `README.md` for complete feature list
   - See `DEVELOPMENT.md` for development guide

2. **Deploy to Production**
   - Follow `DEPLOYMENT.md` guide
   - Deploy backend to Railway
   - Deploy frontend to Vercel

3. **Explore the Code**
   - Backend: `src/main/java/com/anurag/taskmanager/`
   - Frontend: `frontend/src/`

4. **Customize Features**
   - Add email notifications
   - Add file attachments
   - Add user profiles
   - Add team collaboration features

---

## 🚀 One-Command Deploy

Want to deploy to production?

```bash
# Just run these commands and follow the DEPLOYMENT.md guide
git push origin main
# Then link your GitHub repo to Railway and Vercel
```

---

## 💡 Tips

- **Hot Reload**: Both backend and frontend support hot reload. Just save your changes!
- **Database**: You can view the database using pgAdmin:
  ```bash
  docker run -p 5050:80 dpage/pgadmin4
  # Access at http://localhost:5050
  ```
- **API Documentation**: Check `src/main/java/com/anurag/taskmanager/controller/` for all endpoints

---

**Everything working?** 🎉 Congratulations! You're ready to start using Task Manager!

Need help? Check the `README.md` or `DEVELOPMENT.md` files.
