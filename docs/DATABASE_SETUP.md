# Database Architecture & Initializer Guide

This document describes the database layer, schema generation, automated data seeding, and multi-profile database support in the **Research Conference Management System (RCMS)**.

---

## 1. Database Engines & Spring Profiles

RCMS natively supports three database deployment options:

| Database Engine | Target Environment | Active Spring Profile / Driver | Configuration Source |
|:---|:---|:---|:---|
| **MySQL 8.0 (Default)** | Containerized / Production | `com.mysql.cj.jdbc.Driver` | `docker-compose.yml` & `.env` |
| **Neon PostgreSQL** | Cloud PostgreSQL | `org.postgresql.Driver` | `application-postgres.properties` / `.env` |
| **H2 Standalone** | In-Memory / Offline Dev | `org.h2.Driver` | `-Dspring-boot.run.profiles=h2` |

---

## 2. Automated Schema Generation

RCMS uses Spring Data JPA with Hibernate. Database schemas are dynamically created and updated on startup via DDL auto-configuration:

```properties
spring.jpa.hibernate.ddl-auto=update
```

### Core Database Tables:
- `users` — Academic user directory (System Admins, Authors, Reviewers, Attendees)
- `conferences` — Academic conferences and Calls for Papers (CFP)
- `tracks` — Domain research tracks associated with specific conferences
- `papers` — Submitted research manuscripts, abstracts, PDF file paths, and workflow statuses
- `reviews` — Peer evaluation scores, feedback comments, recommendations, and assignments
- `certificates` — Issued digital presentation/attendance credentials with verification codes
- `conference_registrations` — Attendance registration records

---

## 3. Data Initializer (`DataInitializer.java`)

When the application boots, `com.rcms.config.DataInitializer` executes idempotent data seeding via `CommandLineRunner`:

1. **Seeds 25 Academic Users**:
   - **3 System Admins**: Lead Administrator (`admin@rcms.com`), Dr. Sarah Jenkins, Prof. Michael Faraday.
   - **12 Authors**: Dr. Alan Turing (`author@rcms.com`), Dr. Marie Curie, Prof. Richard Feynman, Dr. Barbara Liskov, Dr. Donald Knuth, etc.
   - **10 Reviewers**: Prof. Ada Lovelace (`reviewer@rcms.com`), Prof. Claude Shannon, Dr. Grace Hopper, Prof. Edsger Dijkstra, Dr. Leslie Lamport, etc.
2. **Seeds 2 International Conferences**:
   - *ICCSAI 2026* (International Conference on Computer Science & AI)
   - *ISQC 2026* (IEEE Symposium on Cyber Security & Quantum Cryptography)
3. **Seeds Research Tracks & Submissions**:
   - AI & Machine Learning Track, Distributed Systems Track.
   - Sample paper manuscripts with `UNDER_REVIEW` and `ACCEPTED` statuses.
4. **Seeds Verifiable Certificates**:
   - Certificate Verification Code: `RCMS8888` (assigned to Dr. Barbara Liskov for paper presentation).

---

## 4. Connecting to Cloud PostgreSQL (Neon)

To connect RCMS to a **Neon Cloud PostgreSQL** database:

### Option A: Spring Profile Execution
Pass your database password via environment variable and activate the `postgres` profile:

```cmd
# Windows PowerShell
$env:DB_PASSWORD="your_actual_neon_password"
.\rcms\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=postgres"
```
```bash
# Linux / macOS
DB_PASSWORD="your_actual_neon_password" ./rcms/mvnw spring-boot:run -Dspring-boot.run.profiles=postgres
```

### Option B: `.env` Configuration
Specify connection details directly in `.env`:

```env
DATABASE_URL=jdbc:postgresql://<neon-endpoint>/neondb?sslmode=require
DB_USERNAME=neondb_owner
DB_PASSWORD=your_actual_neon_password
DB_DRIVER=org.postgresql.Driver
DB_DIALECT=org.hibernate.dialect.PostgreSQLDialect
```

---

## 5. Activating the Standalone H2 Profile

Run RCMS completely offline without external database dependencies:

```cmd
# Windows
.\rcms\mvnw.cmd spring-boot:run -f rcms/pom.xml "-Dspring-boot.run.profiles=h2"
```
```bash
# Linux / macOS
./rcms/mvnw spring-boot:run -f rcms/pom.xml -Dspring-boot.run.profiles=h2
```

---

## 6. Resetting Database State

To restore the database to its pristine seeded state:

### When using Docker Compose:
```bash
docker compose down -v
docker compose up --build
```
> [!WARNING]
> `docker compose down -v` permanently removes persistent database volumes (`rcms-mysql-data`).
