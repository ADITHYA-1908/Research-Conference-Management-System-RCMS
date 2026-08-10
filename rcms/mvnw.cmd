@REM ----------------------------------------------------------------------------
@REM Apache Maven Wrapper startup batch script for Windows
@REM ----------------------------------------------------------------------------

@echo off
setlocal enabledelayedexpansion

set "DIRNAME=%~dp0"
if "%DIRNAME%" == "" set "DIRNAME=."
set "JVM_CONFIG=%DIRNAME%\.mvn\jvm.config"

@REM Locate Maven executable or fallback to local bundle
if exist "%DIRNAME%..\apache-maven-3.9.9\bin\mvn.cmd" (
    "%DIRNAME%..\apache-maven-3.9.9\bin\mvn.cmd" %*
    exit /b %ERRORLEVEL%
) else if exist "%DIRNAME%apache-maven-3.9.9\bin\mvn.cmd" (
    "%DIRNAME%apache-maven-3.9.9\bin\mvn.cmd" %*
    exit /b %ERRORLEVEL%
) else (
    where mvn >nul 2>nul
    if %ERRORLEVEL% == 0 (
        mvn %*
        exit /b %ERRORLEVEL%
    ) else (
        echo [ERROR] Maven not found in PATH or local wrapper.
        echo Please ensure JDK 17 and Maven are installed.
        exit /b 1
    )
)
