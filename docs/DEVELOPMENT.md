# Developer Architecture & Engineering Guidelines

This document provides developer guidelines, project architecture overviews, automated test suite execution details, and session security conventions for the **Research Conference Management System (RCMS)**.

---

## 1. Project Architecture

RCMS follows a standard Spring Boot MVC architectural pattern:

```text
com.rcms
 ├── config/            # Web MVC, Session Interceptors, Data Seeding
 ├── controller/        # Spring MVC Page Controllers (Admin, Author, Reviewer, Public)
 ├── dto/               # Data Transfer Objects & Form Binding Objects
 ├── entity/            # JPA Domain Model Entities (User, Conference, Paper, Review, Certificate)
 ├── repository/        # Spring Data JPA Repositories
 └── service/           # Core Business & Workflow Services
```

---

## 2. Running Automated Tests

RCMS includes automated unit and integration tests using Spring Boot Test and an embedded H2 database.

To execute the test suite:

### Maven Wrapper (Windows):
```cmd
.\rcms\mvnw.cmd test -f rcms/pom.xml
```

### Maven Wrapper (Linux / macOS):
```bash
./rcms/mvnw test -f rcms/pom.xml
```

### Expected Test Output:
```text
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

> **Key Test Suites**: Includes `RoleWorkflowIntegrationTest.java` which validates the complete multi-role academic submission and peer evaluation lifecycle.

---

## 3. Session Authentication & Role Authorization

Authentication is enforced globally via `com.rcms.config.AuthInterceptor`:
- Authenticated user state is maintained in the HTTP session under `session.getAttribute("user")`.
- **Public Routes** (`/`, `/login`, `/register`, `/conferences`, `/certificates/verify`): Open to all visitors.
- **Admin Routes** (`/admin/**`): Requires `ADMIN` role.
- **Author Routes** (`/author/**`): Requires `AUTHOR` role.
- **Reviewer Routes** (`/reviewer/**`): Requires `REVIEWER` role.
- Unauthorized route attempts automatically redirect users to `/login`.

---

## 4. File Upload Storage

Uploaded research paper manuscripts are saved to the path specified by `${UPLOAD_DIR}` (default: `uploads/papers`).
- PDF manuscripts submitted by authors are saved to this storage directory.
- Inside Docker containers, this directory is backed by the named volume `rcms-uploads` to preserve uploaded files across container lifecycles.
