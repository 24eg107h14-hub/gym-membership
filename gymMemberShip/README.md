# Gym Membership Management System

A web application to manage gym members and membership plans and to track membership expiry built with **Spring Boot 3 (Java 21)**, **MySQL**, and **React.js (Vite)**.

---

## Run Steps

### 1. Configure Database Credentials
Open `src/main/resources/application.properties` and replace `YOUR_PASSWORD` with your local MySQL password:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gym_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### 2. Start the Backend
Navigate to the backend project directory and launch the Spring Boot application using Maven:
```bash
cd gymMemberShip
./mvnw spring-boot:run
```
*(Or `mvn spring-boot:run` if you have Maven installed globally).*
The backend server runs on `http://localhost:8080`.

### 3. Scaffold & Setup the Frontend
From the root project folder, scaffold the Vite React application:
```bash
npm create vite@latest f -- --template react
```
Copy all files from `f/src/` to your scaffolded `f/src/` directory.

Navigate to `f` and install the required dependencies:
```bash
cd f
npm install
npm install axios react-router-dom
```

### 4. Start the Frontend Dev Server
```bash
npm run dev
```
Open your browser at `http://localhost:5173`.

### 5. Log In
Log in with the seeded Admin credentials:
- **Email:** `admin@gym.com`
- **Password:** `admin123`

---

## Folder Structure

```
GYM PROJECT/
├── gymMemberShip/
│   ├── pom.xml
│   ├── README.md
│   └── src/
│       ├── main/
│       │   ├── java/com/anurag/gymMemberShip/
│       │   │   ├── GymApplication.java
│       │   │   ├── config/
│       │   │   │   ├── DataInitializer.java
│       │   │   │   └── Security.java
│       │   │   ├── controller/
│       │   │   │   ├── AuthController.java
│       │   │   │   ├── DashboardController.java
│       │   │   │   ├── EmployeeController.java
│       │   │   │   ├── MemberController.java
│       │   │   │   ├── PlanController.java
│       │   │   │   └── UserController.java
│       │   │   ├── dto/
│       │   │   │   ├── DashboardStatsDto.java
│       │   │   │   ├── EmployeeDto.java
│       │   │   │   ├── LoginRequestDto.java
│       │   │   │   ├── LoginResponseDto.java
│       │   │   │   ├── MemberDto.java
│       │   │   │   ├── MemberResponseDto.java
│       │   │   │   ├── PlanDto.java
│       │   │   │   └── RegisterRequestDto.java
│       │   │   ├── entity/
│       │   │   │   ├── Member.java
│       │   │   │   ├── MembershipPlan.java
│       │   │   │   └── User.java
│       │   │   ├── enums/
│       │   │   │   ├── MembershipStatus.java
│       │   │   │   └── Role.java
│       │   │   ├── exception/
│       │   │   │   ├── GlobalExceptionHandler.java
│       │   │   │   └── ResourceNotFoundException.java
│       │   │   ├── filter/
│       │   │   │   └── JwtAuthenticationFilter.java
│       │   │   ├── repository/
│       │   │   │   ├── MemberRepository.java
│       │   │   │   ├── MembershipPlanRepository.java
│       │   │   │   └── UserRepository.java
│       │   │   └── service/
│       │   │       ├── AuthService.java
│       │   │       ├── DashboardService.java
│       │   │       ├── EmployeeService.java
│       │   │       ├── JwtService.java
│       │   │       ├── MemberService.java
│       │   │       └── PlanService.java
│       │   └── resources/
│       │       └── application.properties
│       └── test/
│           └── java/com/anurag/gymMemberShip/
│               └── GymMemberShipApplicationTests.java
│
└── f/
    └── src/
        ├── Api.jsx
        ├── App.css
        ├── App.jsx
        ├── Dashboard.jsx
        ├── Employees.jsx
        ├── index.css
        ├── Login.jsx
        ├── MemberForm.jsx
        ├── Members.jsx
        ├── MyMembership.jsx
        ├── Navbar.jsx
        ├── Plans.jsx
        ├── ProtectedRoute.jsx
        ├── Register.jsx
        └── StatusBadge.jsx
```

---

## API List

### 1. Authentication (`/api/auth`)
| Method | Endpoint | Access | Description |
|---|---|---|---|
| `POST` | `/api/auth/register` | Public | Register a new user (`USER` role) |
| `POST` | `/api/auth/login` | Public | Login with email & password, returns JWT |

### 2. Plans (`/api/plans`)
| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/plans` | Authenticated | List all membership plans |
| `POST` | `/api/plans` | ADMIN | Create a new membership plan |
| `PUT` | `/api/plans/{id}` | ADMIN | Update an existing membership plan |
| `DELETE` | `/api/plans/{id}` | ADMIN | Delete a plan (rejected if assigned to members) |

### 3. Employees (`/api/employees`)
| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/employees` | ADMIN | List all employees (passwords hidden) |
| `POST` | `/api/employees` | ADMIN | Create a new employee account |
| `DELETE` | `/api/employees/{id}` | ADMIN | Delete an employee account |

### 4. Members (`/api/members`)
| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/members` | ADMIN, EMPLOYEE | Search & filter members by status/keyword |
| `GET` | `/api/members/{id}` | ADMIN, EMPLOYEE | Retrieve single member details by ID |
| `POST` | `/api/members` | ADMIN, EMPLOYEE | Register new member & calculate expiry |
| `PUT` | `/api/members/{id}` | ADMIN, EMPLOYEE | Update member (recomputes expiry if plan changes) |
| `DELETE` | `/api/members/{id}` | ADMIN | Delete member record |
| `POST` | `/api/members/{id}/renew` | ADMIN, EMPLOYEE | Renew membership (optional `planId` param) |

### 5. Dashboard (`/api/dashboard`)
| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/dashboard/stats` | ADMIN, EMPLOYEE | Member count metrics (total, active, expiring, expired) |

### 6. User Membership (`/api/user`)
| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/user/membership` | USER | View own membership details linked by email |
