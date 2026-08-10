# Research Conference Management System (RCMS)

[![Live Demo](https://img.shields.io/badge/🌐_Live_Demo-rcms--c4je.onrender.com-00C853?style=for-the-badge&logo=render&logoColor=white)](https://rcms-c4je.onrender.com)

[![Java 17](https://img.shields.io/badge/Java-17-blue.svg?style=flat-square&logo=openjdk)](https://adoptium.net/)
[![Spring Boot 3.3.4](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen.svg?style=flat-square&logo=springboot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Neon_Cloud-336791.svg?style=flat-square&logo=postgresql&logoColor=white)](https://neon.tech/)
[![Docker Compose](https://img.shields.io/badge/Docker%20Compose-v3.8-blue.svg?style=flat-square&logo=docker)](https://docs.docker.com/compose/)
[![Render](https://img.shields.io/badge/Deployed_on-Render-46E3B7.svg?style=flat-square&logo=render&logoColor=white)](https://render.com/)
[![Bootstrap 5](https://img.shields.io/badge/Bootstrap-5.3.2-purple.svg?style=flat-square&logo=bootstrap)](https://getbootstrap.com/)
[![License](https://img.shields.io/badge/License-Academic-orange.svg?style=flat-square)](#license)

A modern, full-featured enterprise academic platform designed for managing university research conferences: manuscript submission, double-blind peer evaluations, proceedings indexing, scheduling, and verifiable digital certificates.

> 🌐 **Live Application**: **[https://rcms-c4je.onrender.com](https://rcms-c4je.onrender.com)**

---

## 📋 Executive Overview

RCMS streamlines academic conference operations across four core user personas:

- **🌐 Public & Guest Visitors**: Discover open conferences, review Calls for Papers (CFP), and verify issued digital certificates instantly via security hashes or QR codes.
- **✍️ Research Authors**: Submit PDF research manuscripts to domain tracks, monitor review progress, access peer evaluation feedback, withdraw papers, and download certificates.
- **🔍 Peer Reviewers**: Evaluate assigned submissions using scored rubrics (1–10 rating), submit technical comments, and provide acceptance/rejection recommendations.
- **🛡️ System Administrators**: Full administrative governance to manage conferences & subject tracks, track real-time portal statistics, assign reviewers, record chair decisions, and manage user accounts.

---

## 🛠️ Technology Stack

| Component | Technologies & Versions |
|:---|:---|
| **Backend Core** | Java 17, Spring Boot 3.3.4, Spring Data JPA, Hibernate ORM |
| **Frontend UI** | Thymeleaf, HTML5, Vanilla CSS3, Bootstrap 5.3.2, Bootstrap Icons |
| **Database Layer** | PostgreSQL (Neon Cloud — Production), MySQL 8.0 (Docker), H2 In-Memory (`h2` profile for offline testing) |
| **Build & Tooling** | Apache Maven 3.9.x, Maven Wrapper (`mvnw` / `mvnw.cmd`) |
| **Containerization** | Docker Engine 20.10+, Docker Compose v3.8, Multi-stage secure build |
| **Cloud Deployment** | Render (Web Service), Neon PostgreSQL (Serverless Database) |

---

## 🚀 Quick Start — Running with Docker (Recommended)

Run the entire system (MySQL 8.0 + Spring Boot Web Application) in containerized mode with a single command:

```bash
# 1. Clone the repository
git clone <repository-url>
cd Research-Conference-Management-System

# 2. Copy the environment configuration template
cp .env.example .env

# 3. Build and launch all services with Docker Compose
docker compose up --build
```

Access the application at: **`http://localhost:8080`**

To stop and remove containers:
```bash
docker compose down
```

---

## 💻 Local Development Setup

### Automated System Diagnostic Check
Verify local Java, Maven, Docker, and port availability before running:

```powershell
# Windows PowerShell
.\scripts\check-prerequisites.ps1
```
```bash
# Linux / macOS
./scripts/check-prerequisites.sh
```

### Option A: Standard Execution (Local MySQL)
```cmd
# Windows (One-click batch launcher)
start.bat
```
```bash
# Linux / macOS (Shell script)
./scripts/run-local.sh
```

### Option B: Standalone H2 In-Memory Execution (Zero DB Dependency)
Run the application completely offline using the embedded H2 database:

```cmd
# Windows
.\rcms\mvnw.cmd spring-boot:run -f rcms/pom.xml "-Dspring-boot.run.profiles=h2"
```
```bash
# Linux / macOS
./rcms/mvnw spring-boot:run -f rcms/pom.xml -Dspring-boot.run.profiles=h2
```

---

## 🔑 Pre-Seeded Demo Credentials

The platform includes **25 pre-seeded user accounts** across all roles for immediate evaluation:

| Role | Email Address | Password | Name & Focus Area |
|:---|:---|:---|:---|
| 🛡️ **ADMIN** | `admin@rcms.com` | `admin123` | Lead System Administrator |
| 🛡️ **ADMIN** | `admin2@rcms.com` | `admin123` | Dr. Sarah Jenkins (Director of Academic Events) |
| ✍️ **AUTHOR** | `author@rcms.com` | `password123` | Dr. Alan Turing (Computer Science & AI) |
| ✍️ **AUTHOR** | `author4@rcms.com` | `password123` | Dr. Barbara Liskov (Distributed Systems) |
| 🔍 **REVIEWER** | `reviewer@rcms.com` | `password123` | Prof. Ada Lovelace (Software Engineering) |
| 🔍 **REVIEWER** | `reviewer3@rcms.com` | `password123` | Dr. Grace Hopper (Compiler Architecture) |

> 🏷️ **Demo Certificate Code**: **`RCMS8888`** (Searchable on the home page verification widget).

---

## 📱 Responsive & Mobile First UI

RCMS features a responsive academic layout optimized for all device viewports:
- **Smart Mobile Back Navigation**: Dedicated mobile back buttons (`<- Back`) on **every page** for all roles, automatically excluding only the Home (`/`) and Login (`/login`) pages.
- **Contextual Mobile Headers**: Top navbar and context sub-bars adapt dynamically to user roles (Admin, Author, Reviewer, Guest).
- **Touch-Friendly Controls**: Responsive tables with horizontal scroll containers, fluid metric cards, and mobile navigation togglers.

---

## 📂 Repository Layout

```text
Research-Conference-Management-System/
├── Dockerfile                     # Multi-stage container build definition
├── docker-compose.yml             # MySQL 8.0 & Spring Boot multi-container orchestration
├── .dockerignore                  # Docker build exclusion rules
├── .env.example                   # Environment configuration template
├── README.md                      # Primary developer documentation & quickstart
├── start.bat                      # Windows one-click launcher
│
├── docs/                          # In-depth system documentation
│   ├── USER_GUIDE_AND_TEST_DATA.md # Detailed user guide, demo flow & test data
│   ├── DOCKER_SETUP.md            # Comprehensive Docker & container lifecycle guide
│   ├── SETUP.md                   # Developer environment setup guide
│   ├── DATABASE_SETUP.md          # MySQL schema & H2 database configuration
│   ├── DEVELOPMENT.md             # Architecture patterns & testing guidelines
│   ├── TROUBLESHOOTING.md         # Diagnostic & resolution guide for setup errors
│   └── MANUAL_TEST_CASES.md       # Quality assurance manual test suites
│
├── scripts/                       # Automation & diagnostic utility scripts
│   ├── check-prerequisites.ps1    # Automated system check (Windows)
│   ├── check-prerequisites.sh     # Automated system check (Linux/macOS)
│   ├── run-local.ps1              # Local app launcher (Windows)
│   └── run-local.sh               # Local app launcher (Linux/macOS)
│
└── rcms/                          # Spring Boot application module (Maven Root)
    ├── pom.xml                    # Maven project dependencies & build configuration
    ├── mvnw / mvnw.cmd            # Apache Maven wrapper scripts
    └── src/                       # Application source code & Thymeleaf templates
```

---

## 📚 Complete Documentation Index

- 📘 [User Guide & Demo Walkthrough](docs/USER_GUIDE_AND_TEST_DATA.md)
- 🐳 [Docker & Container Guide](docs/DOCKER_SETUP.md)
- 📖 [Developer Setup Manual](docs/SETUP.md)
- 🛢️ [Database Setup & Profile Guide](docs/DATABASE_SETUP.md)
- 🛠️ [Development Architecture & Guidelines](docs/DEVELOPMENT.md)
- ❓ [Troubleshooting & Diagnostics Guide](docs/TROUBLESHOOTING.md)
- 🧪 [Manual Quality Assurance Test Cases](docs/MANUAL_TEST_CASES.md)

---

## 🌐 Live Demo & Cloud Deployment

| Component | Technology | Details |
|:---|:---|:---|
| **Live URL** | [rcms-c4je.onrender.com](https://rcms-c4je.onrender.com) | Production deployment |
| **Cloud Platform** | Render | Docker-based Web Service |
| **Database** | Neon PostgreSQL | Serverless cloud-native PostgreSQL |
| **Backend** | Spring Boot 3.3.4 | Java 17, Spring Data JPA, Hibernate ORM |
| **Frontend** | Thymeleaf + Bootstrap 5 | Server-side rendered responsive UI |
| **Containerization** | Docker | Multi-stage build with non-root security |

> ⚡ **Note**: The Render free tier may spin down after inactivity. The first request after idle may take 30–60 seconds to cold-start.

---

## 👥 Team Contributions

| Team Member | GitHub | Key Contributions |
|:---|:---|:---|
| **Arjun S** | [@ARJUN-AIML](https://github.com/ARJUN-AIML) | Developer |
| **Adithya Natarajan M** | [@ADITHYA-1908](https://github.com/ADITHYA-1908) | Co-Developer |
| **Ajay Kumar M** | *[GitHub TBD]* | Co-Developer |
| **Kamalesh K** | [@Kamalesh-aiml](https://github.com/Kamalesh-aiml) | Co-Developer |

---

## 📄 License

Developed for Academic Research & University Conference Operations. All rights reserved.
