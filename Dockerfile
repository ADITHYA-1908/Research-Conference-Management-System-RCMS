# ==============================================================================
# Research Conference Management System (RCMS)
# Multi-stage Dockerfile for Production-Ready Container Deployment
# ==============================================================================

# ------------------------------------------------------------------------------
# Stage 1: Build & Package Application
# ------------------------------------------------------------------------------
FROM maven:3.9-eclipse-temurin-17-alpine AS builder
WORKDIR /app

# Copy Maven dependency descriptor first for efficient layer caching
COPY rcms/pom.xml ./
RUN mvn dependency:go-offline -B

# Copy application source code and compile production JAR package
COPY rcms/src ./src
RUN mvn clean package -DskipTests

# ------------------------------------------------------------------------------
# Stage 2: Minimal Production Runtime
# ------------------------------------------------------------------------------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Security: Create non-root system group and user
RUN addgroup -S rcmsgroup && adduser -S rcmsuser -G rcmsgroup

# Storage: Create directory for uploaded research manuscripts and assign ownership
RUN mkdir -p /app/uploads && chown -R rcmsuser:rcmsgroup /app

# Copy compiled executable JAR from builder stage
COPY --from=builder --chown=rcmsuser:rcmsgroup /app/target/research-conference-management-system-1.0.0.jar app.jar

# Switch to unprivileged execution user
USER rcmsuser

# Default application environment configuration
ENV SERVER_PORT=8080 \
    SPRING_PROFILES_ACTIVE=prod \
    UPLOAD_DIR=/app/uploads

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=25s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/ || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
