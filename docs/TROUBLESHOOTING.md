# Setup & Diagnostic Troubleshooting Guide

This guide covers common setup issues, diagnostic commands, and step-by-step resolution steps for onboarding and operating **RCMS**.

---

## 1. Port 8080 Conflict (`Port 8080 was already in use`)

### Symptom:
```text
Web server failed to start. Port 8080 was already in use.
```

### Resolution:

#### Windows (PowerShell):
Identify the PID occupying port 8080 and release it:
```powershell
Get-NetTCPConnection -LocalPort 8080 | Select-Object OwningProcess
Stop-Process -Id <PID> -Force
```
*Or execute `start.bat`, which automatically detects and releases port 8080.*

#### Linux / macOS:
```bash
sudo lsof -i :8080
sudo kill -9 <PID>
```

#### Alternative: Change Server Port
Specify an alternate port in your `.env` file:
```env
SERVER_PORT=8081
```

---

## 2. Docker Container Restart Loop (`rcms-app` or `db`)

### Symptom:
`docker compose ps` displays status `Restarting` or `Unhealthy`.

### Resolution:
Stream container logs to diagnose the underlying error:
```bash
docker compose logs db
docker compose logs rcms-app
```

#### Common Causes & Fixes:
1. **Docker Engine Memory Limits**: Ensure Docker Desktop has at least 2GB RAM allocated.
2. **Host Port 3306 Conflict**: If a local MySQL service is running on port 3306 of your host, update `.env`:
   ```env
   DB_PORT=3307
   ```

---

## 3. Java Version Mismatch (`UnsupportedClassVersionError`)

### Symptom:
```text
java.lang.UnsupportedClassVersionError: com/rcms/RcmsApplication has been compiled by a more recent version of Java (class file version 61.0)
```

### Resolution:
Your active `java` version is JDK 11 or earlier. RCMS requires JDK 17+ (class file version 61.0).

1. Verify installed JDK version:
   ```bash
   java -version
   ```
2. Ensure `JAVA_HOME` points to JDK 17+ and the JDK 17 `bin` directory is prioritized in system `PATH`.

---

## 4. Maven Command Failure (`mvn` not recognized)

### Symptom:
```text
'mvn' is not recognized as an internal or external command
```

### Resolution:
Use the repository's embedded Maven wrapper scripts (`mvnw` / `mvnw.cmd`):
- **Windows**: `.\rcms\mvnw.cmd spring-boot:run -f rcms/pom.xml`
- **Linux/macOS**: `./rcms/mvnw spring-boot:run -f rcms/pom.xml`

---

## 5. MySQL Access Denied / Bad Credentials

### Symptom:
```text
java.sql.SQLException: Access denied for user 'rcms_user'@'localhost'
```

### Resolution:
1. Verify credentials in `.env`:
   ```env
   DB_USERNAME=rcms_user
   DB_PASSWORD=rcms_password_2026
   ```
2. Reset containerized database volumes if credentials were changed after initial boot:
   ```bash
   docker compose down -v
   docker compose up --build
   ```

---

## 6. Upload Directory Permissions

### Symptom:
```text
java.io.IOException: Permission denied creating directory uploads/papers
```

### Resolution:
- **Local Dev**: Ensure your operating system account has write permissions in the project workspace.
- **Docker**: Container permissions are handled automatically via the `rcmsuser` unprivileged account.
