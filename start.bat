@echo off
setlocal enabledelayedexpansion
title Research Conference Management System (RCMS)
color 0A

echo ==================================================================
echo   RESEARCH CONFERENCE MANAGEMENT SYSTEM (RCMS) - ACADEMIC PORTAL
echo ==================================================================
echo.

:: Set root directory cleanly
set "ROOT_DIR=%~dp0"
cd /d "%ROOT_DIR%"

:: Auto-release Port 8080 if occupied
echo [INFO] Checking port 8080 availability...
netstat -ano | findstr /R /C:":8080 .*LISTENING" >nul 2>&1
if !errorlevel! == 0 (
    echo [INFO] Port 8080 is currently in use. Releasing port...
    for /f "tokens=5" %%P in ('netstat -ano ^| findstr /R /C:":8080 .*LISTENING"') do (
        taskkill /F /PID %%P >nul 2>&1
    )
    timeout /t 1 >nul
)

echo [INFO] Starting Spring Boot application...
echo [INFO] Application will be available at http://localhost:8080 once server is ready...
echo ------------------------------------------------------------------
echo.

:: Auto-launch default browser after 12 seconds delay
start /b "" cmd /c "timeout /t 12 >nul && start http://localhost:8080"

:: Execute Maven Wrapper cleanly
call "%ROOT_DIR%rcms\mvnw.cmd" spring-boot:run -f "%ROOT_DIR%rcms\pom.xml"

if !errorlevel! neq 0 (
    echo.
    echo ==================================================================
    echo [ERROR] Application stopped or failed to start.
    echo         Please verify JDK 17+ is installed and configured in PATH.
    echo ==================================================================
)

pause
