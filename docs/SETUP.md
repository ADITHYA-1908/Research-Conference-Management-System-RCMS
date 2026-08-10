# Developer Onboarding & Environment Setup Guide

Welcome to the **Research Conference Management System (RCMS)**! This document provides a step-by-step walkthrough for configuring your development environment from a fresh clone or unzipped workspace.

---

## 1. Prerequisites & System Requirements

Ensure your local development workstation meets the following minimum requirements:

### Required Software Tools
| Tool | Required Version | Installation Reference |
|:---|:---|:---|
| **Java Development Kit (JDK)** | **17 or higher** (Temurin / Corretto / Oracle) | [Adoptium JDK 17 Download](https://adoptium.net/) |
| **Docker Engine & Desktop** | 20.10+ / Compose v2.0+ | [Docker Desktop Download](https://www.docker.com/products/docker-desktop/) |
| **Git** | 2.30+ | [Git Official Download](https://git-scm.com/) |

### Recommended Developer Tools
- **IDE**: IntelliJ IDEA 2023+, VS Code (with Java Extension Pack), or Eclipse IDE.
- **Database Client**: DBeaver, MySQL Workbench, or DataGrip.
- **API Client**: Postman or Thunder Client.

---

## 2. Automated Environment Verification

Before running the application, verify your host environment using our built-in pre-flight diagnostic script:

### Windows (PowerShell):
```powershell
.\scripts\check-prerequisites.ps1
```

### Linux / macOS (Bash):
```bash
chmod +x ./scripts/check-prerequisites.sh
./scripts/check-prerequisites.sh
```

---

## 3. Environment Configuration (`.env`)

Create your local environment configuration file by copying `.env.example`:

### Windows (Command Prompt / PowerShell):
```powershell
copy .env.example .env
```

### Linux / macOS:
```bash
cp .env.example .env
```

### Primary Environment Variables:
```env
# Web Server Configuration
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=dev

# MySQL Container Configuration
MYSQL_DATABASE=rcms_db
MYSQL_USER=rcms_user
MYSQL_PASSWORD=rcms_password_2026
MYSQL_ROOT_PASSWORD=rcms_root_password_2026

# Host Database Connection
DB_HOST=localhost
DB_PORT=3306
DB_NAME=rcms_db
DB_USERNAME=rcms_user
DB_PASSWORD=rcms_password_2026

# Uploaded Manuscripts Storage Path
UPLOAD_DIR=uploads/papers
```

---

## 4. Running the Application

Choose one of two supported execution methods:

### Option A: Recommended — Docker Compose (Full Stack with MySQL)
Launches the MySQL 8.0 database container and Spring Boot application with built-in health checks:

```bash
docker compose up --build
```
Access the application at: **`http://localhost:8080`**

---

### Option B: Local Execution (Without Docker)

#### Windows One-Click Launcher:
```cmd
.\start.bat
```

#### PowerShell / Shell Launchers:
```powershell
# Windows
.\scripts\run-local.ps1
```
```bash
# Linux / macOS
./scripts/run-local.sh
```

*Note: If local MySQL is not detected on port 3306, the launcher automatically falls back to an embedded H2 database for instant execution.*

---

## 5. Post-Launch Verification Checklist

After starting the application, perform these quick verification steps:

1. **Web Accessibility**: Open [http://localhost:8080](http://localhost:8080) in your browser. Verify the RCMS home page renders cleanly.
2. **Authentication Check**: Click **Sign In** and use one of the pre-seeded credentials:
   - **Admin**: `admin@rcms.com` / `admin123`
   - **Author**: `author@rcms.com` / `password123`
   - **Reviewer**: `reviewer@rcms.com` / `password123`
3. **Database Health**: Confirm statistics cards on the home page display active seeded conferences (e.g., ICCSAI 2026).
4. **Certificate Lookup**: Enter verification code `RCMS8888` on the home page certificate search widget.

---

## 6. Detailed System Documentation

- 🐳 [Docker Infrastructure Guide](DOCKER_SETUP.md)
- 🛢️ [Database Architecture & Profiles](DATABASE_SETUP.md)
- 📘 [User Guide & Test Data Manual](USER_GUIDE_AND_TEST_DATA.md)
- 🛠️ [Development & Architecture Guidelines](DEVELOPMENT.md)
- ❓ [Troubleshooting & Diagnostics](TROUBLESHOOTING.md)
