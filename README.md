# School Management System
A full-stack web application for managing students, teachers, and courses built with **Spring Boot**, **PostgreSQL**, and **Docker**.

---

## 🚀 Quick Navigation to Code

**Need to see the code? Click on any file below:**

### Backend Code (Java)
| Component | File | Description |
|-----------|------|-------------|
| 🎯 **Main App** | [SepmAssignmentApplication.java](src/main/java/com/example/sepm_assignment/SepmAssignmentApplication.java) | Application entry point |
| 🔐 **Security** | [SecurityConfig.java](src/main/java/com/example/sepm_assignment/config/SecurityConfig.java) | Authentication setup |
| 📡 **Controllers** | [TeacherController](src/main/java/com/example/sepm_assignment/controller/TeacherController.java) • [StudentController](src/main/java/com/example/sepm_assignment/controller/StudentController.java) • [CourseController](src/main/java/com/example/sepm_assignment/controller/CourseController.java) | REST API endpoints |
| 📦 **Models** | [Teacher](src/main/java/com/example/sepm_assignment/model/Teacher.java) • [Student](src/main/java/com/example/sepm_assignment/model/Student.java) • [Course](src/main/java/com/example/sepm_assignment/model/Course.java) | Database entities |
| 🗄️ **Repositories** | [TeacherRepository](src/main/java/com/example/sepm_assignment/repository/TeacherRepository.java) • [StudentRepository](src/main/java/com/example/sepm_assignment/repository/StudentRepository.java) • [CourseRepository](src/main/java/com/example/sepm_assignment/repository/CourseRepository.java) | Data access layer |
| 💼 **Services** | [TeacherService](src/main/java/com/example/sepm_assignment/service/TeacherService.java) • [StudentService](src/main/java/com/example/sepm_assignment/service/StudentService.java) • [CourseService](src/main/java/com/example/sepm_assignment/service/CourseService.java) | Business logic |

### Frontend Code
| File | Description |
|------|-------------|
| [**index.html**](src/main/resources/static/index.html) | Modern UI with login page |
| [**app.js**](src/main/resources/static/app.js) | JavaScript API integration |

### Configuration Files
| File | Purpose |
|------|---------|
| [**application.properties**](src/main/resources/application.properties) | Database & server config |
| [**pom.xml**](pom.xml) | Maven dependencies |
| [**Dockerfile**](Dockerfile) | Container build |
| [**compose.yaml**](compose.yaml) | Docker setup |

### Testing
| File | Purpose |
|------|---------|
| [**API_TEST.md**](API_TEST.md) | API testing with curl |
| [**postman_collection.json**](postman_collection.json) | Postman collection |

---
## Table of Contents
1. [Project Overview](#project-overview)
2. [Architecture](#architecture)
3. [Technologies Used](#technologies-used)
4. [Project Structure](#project-structure)
5. [Database Design](#database-design)
6. [REST API Endpoints](#rest-api-endpoints)
7. [Security](#security)
8. [How to Run](#how-to-run)
9. [Docker Configuration](#docker-configuration)
10. [Frontend](#frontend)
---
## Project Overview
This School Management System is a web-based application that allows administrators to manage:
- **Teachers** - Add, view, update, and delete teacher records
- **Students** - Manage student information with teacher assignments
- **Courses** - Create and manage courses taught by teachers
The application follows the **MVC (Model-View-Controller)** architecture pattern and implements **RESTful API** principles.
---
## Architecture
### MVC Pattern
`
+-------------------------------------------------------------+
|                        CLIENT (Browser)                      |
|                    index.html + app.js + style.css           |
+-----------------------------+-------------------------------+
                              | HTTP Requests (REST API)
                              v
+-------------------------------------------------------------+
|                     CONTROLLER LAYER                         |
|   StudentController, TeacherController, CourseController     |
|   - Handles HTTP requests                                    |
|   - Maps URLs to methods                                     |
|   - Returns JSON responses                                   |
+-----------------------------+-------------------------------+
                              |
                              v
+-------------------------------------------------------------+
|                      SERVICE LAYER                           |
|      StudentService, TeacherService, CourseService           |
|   - Contains business logic                                  |
|   - Handles data processing                                  |
|   - Manages transactions                                     |
+-----------------------------+-------------------------------+
                              |
                              v
+-------------------------------------------------------------+
|                    REPOSITORY LAYER                          |
|   StudentRepository, TeacherRepository, CourseRepository     |
|   - JPA/Hibernate ORM                                        |
|   - Database CRUD operations                                 |
+-----------------------------+-------------------------------+
                              |
                              v
+-------------------------------------------------------------+
|                      DATABASE (PostgreSQL)                   |
|              students, teachers, courses tables              |
+-------------------------------------------------------------+
`
### Layered Architecture Explanation
| Layer | Component | Responsibility |
|-------|-----------|----------------|
| **Model** | Student.java, Teacher.java, Course.java | Entity classes representing database tables |
| **View** | index.html, style.css, app.js | Frontend UI (Single Page Application) |
| **Controller** | *Controller.java | Handle HTTP requests, route to services |
| **Service** | *Service.java | Business logic implementation |
| **Repository** | *Repository.java | Database access using Spring Data JPA |
---
## Technologies Used
### Backend
| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17 | Programming language |
| Spring Boot | 3.2.5 | Application framework |
| Spring Data JPA | 3.2.5 | Database ORM |
| Spring Security | 6.2.4 | Authentication and Authorization |
| Hibernate | 6.4.4 | JPA Implementation |
| PostgreSQL | 16 | Relational Database |
| Maven | 3.9.6 | Build tool and Dependency management |
### Frontend
| Technology | Purpose |
|------------|---------|
| HTML5 | Page structure |
| CSS3 | Styling and Layout |
| JavaScript (ES6) | Dynamic functionality |
| Fetch API | REST API communication |
### DevOps
| Technology | Purpose |
|------------|---------|
| Docker | Containerization |
| Docker Compose | Multi-container orchestration |
---
## Project Structure
`
sepm_assignment/
|
+-- src/
|   +-- main/
|       +-- java/com/example/sepm_assignment/
|       |   +-- SepmAssignmentApplication.java    # Main entry point
|       |   +-- config/
|       |   |   +-- SecurityConfig.java           # Security configuration
|       |   +-- controller/
|       |   |   +-- StudentController.java        # Student REST endpoints
|       |   |   +-- TeacherController.java        # Teacher REST endpoints
|       |   |   +-- CourseController.java         # Course REST endpoints
|       |   +-- model/
|       |   |   +-- Student.java                  # Student entity
|       |   |   +-- Teacher.java                  # Teacher entity
|       |   |   +-- Course.java                   # Course entity
|       |   +-- repository/
|       |   |   +-- StudentRepository.java        # Student data access
|       |   |   +-- TeacherRepository.java        # Teacher data access
|       |   |   +-- CourseRepository.java         # Course data access
|       |   +-- service/
|       |       +-- StudentService.java           # Student business logic
|       |       +-- TeacherService.java           # Teacher business logic
|       |       +-- CourseService.java            # Course business logic
|       +-- resources/
|           +-- application.properties            # App configuration
|           +-- static/
|               +-- index.html                    # Main HTML page
|               +-- app.js                        # JavaScript logic
|               +-- style.css                     # CSS styles
|
+-- Dockerfile                                    # Docker image build
+-- compose.yaml                                  # Docker Compose config
+-- pom.xml                                       # Maven dependencies
+-- README.md                                     # This file
`

### Direct Links to Source Code Files

#### Main Application
- [**SepmAssignmentApplication.java**](src/main/java/com/example/sepm_assignment/SepmAssignmentApplication.java) - Main Spring Boot application entry point

#### Configuration
- [**SecurityConfig.java**](src/main/java/com/example/sepm_assignment/config/SecurityConfig.java) - Spring Security configuration with Basic Auth

#### Controllers (REST API Layer)
- [**StudentController.java**](src/main/java/com/example/sepm_assignment/controller/StudentController.java) - Student CRUD endpoints
- [**TeacherController.java**](src/main/java/com/example/sepm_assignment/controller/TeacherController.java) - Teacher CRUD endpoints
- [**CourseController.java**](src/main/java/com/example/sepm_assignment/controller/CourseController.java) - Course CRUD endpoints

#### Models (Entity Classes)
- [**Student.java**](src/main/java/com/example/sepm_assignment/model/Student.java) - Student entity with JPA annotations
- [**Teacher.java**](src/main/java/com/example/sepm_assignment/model/Teacher.java) - Teacher entity with relationships
- [**Course.java**](src/main/java/com/example/sepm_assignment/model/Course.java) - Course entity

#### Repositories (Data Access Layer)
- [**StudentRepository.java**](src/main/java/com/example/sepm_assignment/repository/StudentRepository.java) - Student database operations
- [**TeacherRepository.java**](src/main/java/com/example/sepm_assignment/repository/TeacherRepository.java) - Teacher database operations
- [**CourseRepository.java**](src/main/java/com/example/sepm_assignment/repository/CourseRepository.java) - Course database operations

#### Services (Business Logic Layer)
- [**StudentService.java**](src/main/java/com/example/sepm_assignment/service/StudentService.java) - Student business logic
- [**TeacherService.java**](src/main/java/com/example/sepm_assignment/service/TeacherService.java) - Teacher business logic
- [**CourseService.java**](src/main/java/com/example/sepm_assignment/service/CourseService.java) - Course business logic

#### Frontend
- [**index.html**](src/main/resources/static/index.html) - Main HTML page with modern UI
- [**app.js**](src/main/resources/static/app.js) - JavaScript logic for API calls
- [**style.css**](src/main/resources/static/style.css) - CSS styling (if exists)

#### Configuration Files
- [**application.properties**](src/main/resources/application.properties) - Database and server configuration
- [**pom.xml**](pom.xml) - Maven dependencies and build configuration
- [**Dockerfile**](Dockerfile) - Docker container build instructions
- [**compose.yaml**](compose.yaml) - Docker Compose multi-container setup

#### Testing & Documentation
- [**API_TEST.md**](API_TEST.md) - Complete API testing guide with curl commands
- [**postman_collection.json**](postman_collection.json) - Postman collection for API testing
---
## Database Design
### Entity Relationship Diagram (ERD)
`
+------------------+       +------------------+       +------------------+
|     TEACHER      |       |     STUDENT      |       |     COURSE       |
+------------------+       +------------------+       +------------------+
| id (PK)          |       | id (PK)          |       | id (PK)          |
| name             |       | name             |       | title            |
| email            |       | email            |       | code             |
| department       |       | student_id       |       | credits          |
+--------+---------+       | teacher_id (FK)  |       | teacher_id (FK)  |
         |                 +--------+---------+       +--------+---------+
         |                          |                          |
         |    ONE-TO-MANY           |                          |
         +--------------------------+                          |
         |                                                     |
         |    ONE-TO-MANY                                      |
         +-----------------------------------------------------+
`
### Relationships
1. **Teacher to Students (One-to-Many)**
   - One teacher can have many students
   - Each student belongs to one teacher (advisor/supervisor)
2. **Teacher to Courses (One-to-Many)**
   - One teacher can teach many courses
   - Each course is taught by one teacher
---
## REST API Endpoints
### Base URL: http://localhost:8081/api
### Teachers API
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /teachers | Get all teachers |
| GET | /teachers/{id} | Get teacher by ID |
| POST | /teachers | Create new teacher |
| PUT | /teachers/{id} | Update teacher |
| DELETE | /teachers/{id} | Delete teacher |
### Students API
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /students | Get all students |
| GET | /students/{id} | Get student by ID |
| POST | /students | Create new student |
| PUT | /students/{id} | Update student |
| DELETE | /students/{id} | Delete student |
### Courses API
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /courses | Get all courses |
| GET | /courses/{id} | Get course by ID |
| POST | /courses | Create new course |
| PUT | /courses/{id} | Update course |
| DELETE | /courses/{id} | Delete course |
---
## Security
### Authentication
The application uses **HTTP Basic Authentication** with **Spring Security**.
### Users Configuration
| Username | Password | Role | Permissions |
|----------|----------|------|-------------|
| admin | adminpass | ADMIN, USER | Full CRUD access |
| user | userpass | USER | Read-only access |
### Protected Endpoints
- All /api/** endpoints require authentication
- Static resources (HTML, CSS, JS) are publicly accessible
---
## How to Run
### Prerequisites
- Docker Desktop installed and running
- Git (optional)
### Quick Start
1. **Start the application using Docker Compose**
   `
   docker compose up -d --build
   `
2. **Wait for containers to start** (about 30-60 seconds)
3. **Open in browser**
   `
   http://localhost:8081
   `
4. **Login with credentials**
   - Admin: admin / adminpass
   - User: user / userpass
### Check Application Status
`
.\check-app.ps1
`
Or
`
.\check-app.bat
`
### Stop the Application
`
docker compose down
`
### View Logs
`
docker logs sepm_assignment-app-1
`
---
## Docker Configuration
### Dockerfile Explanation
The Dockerfile uses multi-stage build:
1. **Build Stage**: Uses Maven to compile and package the application
2. **Run Stage**: Uses lightweight JRE image to run the JAR file
### Docker Compose
The compose.yaml defines two services:
1. **app**: Spring Boot application (port 8081)
2. **postgres**: PostgreSQL database (port 5432)
### Container Communication
- The app container connects to postgres container using internal Docker network
- PostgreSQL data is persisted using Docker volume (pgdata)
---
## Frontend
### Single Page Application (SPA)
The frontend is a Single Page Application built with vanilla JavaScript.
### Features
- Responsive Design - Works on desktop and mobile
- Tab-based Navigation - Teachers, Students, Courses
- Dynamic Data Loading - Fetches data from REST API
- Form Validation - Client-side input validation
- Real-time Feedback - Success/error messages
### Authentication Flow
1. User enters username and password
2. Frontend sends credentials to API with Basic Auth header
3. If valid - Show main content
4. If invalid - Show error message
---
## Summary
This project demonstrates:
- MVC Architecture - Clear separation of concerns
- REST API Principles - Proper HTTP methods and status codes
- PostgreSQL Database - Relational data storage
- One-to-Many Relationships - Teacher to Students, Teacher to Courses
- Spring Security - Authentication and authorization
- Docker Containerization - Easy deployment
- Responsive Frontend - User-friendly interface
---
## Author
SEPM Assignment - School Management System
