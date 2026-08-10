# Docker & Container Lifecycle Guide

This document details the containerization architecture, Docker Compose configuration, multi-stage build structure, and persistent volume management for the **Research Conference Management System (RCMS)**.

---

## 1. Container Architecture Overview

RCMS utilizes a two-tier containerized stack managed via Docker Compose:

```text
               +----------------------------------+
               |        Developer / Client        |
               +----------------------------------+
                                |
                        http://localhost:8080
                                |
                                v
               +----------------------------------+
               |     rcms-app (Spring Boot)       |
               |     Port 8080                    |
               +----------------------------------+
                                |
                    MySQL TCP (Port 3306)
                                |
                                v
               +----------------------------------+
               |     db (MySQL 8.0 Engine)        |
               |     Port 3306                    |
               +----------------------------------+
                                |
                    +-----------+-----------+
                    |                       |
                    v                       v
          rcms-mysql-data             rcms-uploads
         (Persistent Volume)      (Persistent Volume)
```

---

## 2. Docker Compose Commands Reference

### Start All Services (Build & Run)
```bash
docker compose up --build
```
*The Spring Boot web application is accessible at `http://localhost:8080` once the MySQL database health check succeeds.*

### Run Services in Background (Detached Mode)
```bash
docker compose up -d --build
```

### Inspect Container Health & Status
```bash
docker compose ps
```

### Stream Live Container Logs
```bash
# Stream logs from all services
docker compose logs -f

# Stream application container logs only
docker compose logs -f rcms-app

# Stream database container logs only
docker compose logs -f db
```

### Stop Services (Retain Data Volumes)
```bash
docker compose down
```

### Fresh Environment Reset (Wipe Database & Upload Volumes)
> [!WARNING]
> This command completely stops all containers and removes all persistent database volumes. Use this when you want a completely fresh database initialization.

```bash
docker compose down -v
docker compose up --build
```

---

## 3. Multi-Stage Dockerfile Strategy

The root [`Dockerfile`](../Dockerfile) uses a security-hardened multi-stage build design:

1. **Stage 1 (Builder)**: Uses `maven:3.9-eclipse-temurin-17-alpine` to compile source code and package the Spring Boot executable JAR (`-DskipTests`). Maven dependencies are pre-fetched (`mvn dependency:go-offline`) before copying source code to maximize Docker layer caching efficiency.
2. **Stage 2 (Runtime)**: Uses `eclipse-temurin:17-jre-alpine` for a lightweight runtime footprint.
3. **Security**: Runs under an unprivileged system user (`rcmsuser:rcmsgroup`).
4. **Health Check**: Executes a lightweight HTTP GET probe against `http://localhost:8080/` every 30 seconds.

---

## 4. Container Health Checks & Dependency Ordering

The application service enforces strict dependency ordering:

```yaml
depends_on:
  db:
    condition: service_healthy
```

The database container runs `mysqladmin ping` to verify that MySQL has initialized its database engine and accepted TCP socket connections BEFORE Spring Boot starts. This prevents connection timeouts during application startup.
