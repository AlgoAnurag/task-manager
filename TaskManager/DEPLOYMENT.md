# Deployment Guide - Team Task Manager

This guide will help you deploy the Team Task Manager application to Railway and Vercel/Netlify.

## Prerequisites

- GitHub account (repository must be public or private with access)
- Railway account (https://railway.app)
- Vercel or Netlify account
- Credit card (Railway requires for free tier usage)

## Backend Deployment on Railway

### Step 1: Prepare Repository

1. Ensure all code is committed and pushed to GitHub
```bash
cd /Users/KXT87690/Downloads/TaskManager
git add .
git commit -m "Full-stack Task Manager application"
git push origin main
```

2. Update `application.properties` to use environment variables:
```properties
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
app.jwt.secret=${APP_JWT_SECRET}
app.jwt.expiration=${APP_JWT_EXPIRATION:86400000}
app.cors.allowed-origins=${APP_CORS_ALLOWED_ORIGINS:http://localhost:3000,http://localhost:5173}
```

### Step 2: Create Railway PostgreSQL Database

1. Go to https://railway.app and sign in
2. Create new project
3. Select "PostgreSQL" from the template
4. Railway will automatically generate credentials
5. Note the following from Railway dashboard:
   - `DATABASE_URL` (or extract host, port, username, password)

### Step 3: Deploy Backend Application

1. In Railway dashboard, click "+ Create" → "New Service"
2. Select "GitHub Repo"
3. Connect your GitHub account and select the TaskManager repository
4. Railway will detect it's a Maven project
5. Configure environment variables:
   ```
   SPRING_DATASOURCE_URL=jdbc:postgresql://[host]:[port]/[database]
   SPRING_DATASOURCE_USERNAME=[username]
   SPRING_DATASOURCE_PASSWORD=[password]
   APP_JWT_SECRET=your-super-secret-jwt-key-change-this
   APP_JWT_EXPIRATION=86400000
   APP_CORS_ALLOWED_ORIGINS=https://your-frontend-url.vercel.app
   ```

6. Set Java version to 17 if needed
7. Click "Deploy"
8. Railway will build and deploy automatically
9. Once deployed, copy your backend URL (e.g., `https://taskmanager-production.up.railway.app`)

### Step 4: Verify Backend

Test the backend health endpoint:
```bash
curl https://your-railway-url/api/v1/auth/health
# Should return: Server is running
```

## Frontend Deployment on Vercel

### Step 1: Prepare Frontend

1. Update `frontend/.env.production`:
```
REACT_APP_API_URL=https://your-railway-backend-url/api/v1
```

2. Commit changes:
```bash
git add frontend/.env.production
git commit -m "Update production API URL"
git push origin main
```

### Step 2: Deploy to Vercel

1. Go to https://vercel.com and sign in
2. Click "New Project"
3. Import your GitHub repository
4. Select the TaskManager repository
5. Configure build settings:
   - **Framework Preset**: Vite
   - **Root Directory**: `frontend`
   - **Build Command**: `npm run build`
   - **Output Directory**: `dist`
6. Set environment variables:
   ```
   REACT_APP_API_URL=https://your-railway-backend-url/api/v1
   ```
7. Click "Deploy"
8. Wait for deployment to complete
9. Copy your Vercel URL (e.g., `https://taskmanager.vercel.app`)

### Step 3: Update CORS on Backend

1. Go back to Railway dashboard
2. Update `APP_CORS_ALLOWED_ORIGINS` with your Vercel URL:
   ```
   https://your-vercel-app.vercel.app
   ```
3. Redeploy backend

## Alternative: Deploy Frontend to Netlify

1. Go to https://netlify.com and sign in
2. Click "Add new site" → "Import an existing project"
3. Select GitHub and authorize
4. Choose your repository
5. Configure build settings:
   - **Base directory**: `frontend`
   - **Build command**: `npm run build`
   - **Publish directory**: `dist`
6. Add environment variable:
   ```
   REACT_APP_API_URL=https://your-railway-backend-url/api/v1
   ```
7. Click "Deploy"

## Post-Deployment Testing

### 1. Test Authentication

```bash
# Sign up
curl -X POST https://your-vercel-url/api/v1/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "firstName": "Test",
    "lastName": "User",
    "password": "test123456"
  }'

# Login
curl -X POST https://your-vercel-url/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "test123456"
  }'
```

### 2. Test Project Creation

Use the JWT token from login response:

```bash
curl -X POST https://your-vercel-url/api/v1/projects \
  -H "Authorization: Bearer [token]" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Project",
    "description": "Testing deployment"
  }'
```

### 3. Visit Frontend

Open `https://your-vercel-app.vercel.app` in browser and:
1. Sign up with test credentials
2. Create a project
3. Add tasks
4. Check dashboard

## Troubleshooting

### Backend Build Fails

**Issue**: Maven build fails on Railway
- Check if `pom.xml` has correct Java version (should be 17)
- Ensure all dependencies are public (no private repos)

**Fix**:
```bash
# Test locally first
./mvnw clean install -DskipTests
```

### CORS Errors on Frontend

**Issue**: Frontend can't reach backend
- Frontend shows "Access denied" or network errors

**Fix**:
1. Check frontend has correct `REACT_APP_API_URL`
2. Verify backend `APP_CORS_ALLOWED_ORIGINS` includes frontend URL
3. Check browser console for exact error

### Database Connection Issues

**Issue**: Backend can't connect to PostgreSQL
- Ensure Railway PostgreSQL service is running
- Verify credentials in environment variables
- Check connection URL format: `jdbc:postgresql://host:port/database`

### Frontend Build Fails on Vercel

**Issue**: Vite build or npm dependencies fail
- Clear `node_modules`: `rm -rf frontend/node_modules`
- Check `package.json` for duplicate dependencies
- Ensure `vite.config.js` is correct

## Performance Optimization

### Backend
- Enable database connection pooling
- Set appropriate JVM heap size: `JAVA_OPTS=-Xmx512m`
- Use caching for frequently accessed data

### Frontend
- Tailwind CSS is already optimized for production
- Use React.lazy() for code splitting if needed
- Enable gzip compression in Vercel settings

## Security Best Practices

1. **Change JWT Secret**
   - Generate a strong random string
   - Update `APP_JWT_SECRET` in Railway

2. **Enable HTTPS**
   - Both Railway and Vercel provide free HTTPS
   - Verify browser shows 🔒 lock icon

3. **Database Credentials**
   - Never commit `.env` files
   - Use Railway's secret management
   - Rotate credentials monthly

4. **API Rate Limiting** (Optional)
   - Consider adding Spring Security rate limiting
   - Use CloudFlare for DDoS protection

## Monitoring

### Railway Dashboard
- View logs: Project → Logs tab
- Monitor resource usage: Project → Metrics tab
- Check deployment history: Project → Deployments tab

### Vercel Dashboard
- View logs: Project → Deployments → Logs
- Monitor build times and errors
- Check analytics: Project → Analytics

## Rollback Procedure

### If Backend Update Breaks App
1. Go to Railway dashboard
2. Click on your backend service
3. Go to "Deployments" tab
4. Select previous working deployment
5. Click "Redeploy"

### If Frontend Update Breaks App
1. Go to Vercel dashboard
2. Click on your project
3. Go to "Deployments" tab
4. Click "Promote to Production" on previous working deployment

## Next Steps

1. Set up domain names (optional):
   - Railway: Custom domains in project settings
   - Vercel: Add domain in project settings

2. Set up email notifications for deployments

3. Consider adding:
   - Email verification for user signup
   - Forgot password functionality
   - User profile page
   - Admin dashboard

4. Performance monitoring:
   - Add Sentry for error tracking
   - Use New Relic or DataDog for monitoring

---

**Deployment Complete!** Your application is now live and accessible online.

### Live URLs
- **Backend**: `https://your-railway-url/api/v1`
- **Frontend**: `https://your-vercel-app.vercel.app`

Share these URLs with your team and start using the Task Manager!
