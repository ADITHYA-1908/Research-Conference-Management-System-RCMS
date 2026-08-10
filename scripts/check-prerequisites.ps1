# ==============================================================================
# RCMS Pre-flight Diagnostic & Environment Check (Windows PowerShell)
# ==============================================================================

Write-Host "==================================================================" -ForegroundColor Cyan
Write-Host "   RESEARCH CONFERENCE MANAGEMENT SYSTEM (RCMS) - PRE-FLIGHT CHECK" -ForegroundColor Cyan
Write-Host "==================================================================" -ForegroundColor Cyan
Write-Host ""

$errorsCount = 0

# 1. Check Java JDK Installation
Write-Host "[1/5] Checking Java JDK..." -NoNewline
try {
    $javaVerOutput = & java -version 2>&1 | Out-String
    if ($javaVerOutput -match 'version "(\d+)') {
        $majorVer = [int]$matches[1]
        if ($majorVer -ge 17) {
            Write-Host " [OK] (Detected Java $majorVer)" -ForegroundColor Green
        } else {
            Write-Host " [FAIL] (Detected Java $majorVer, but JDK 17+ is required)" -ForegroundColor Red
            $errorsCount++
        }
    } else {
        Write-Host " [FAIL] (Java executable found, but version could not be parsed)" -ForegroundColor Red
        $errorsCount++
    }
} catch {
    Write-Host " [FAIL] (Java is not installed or not in PATH)" -ForegroundColor Red
    Write-Host "        --> Please download and install JDK 17+ from https://adoptium.net/" -ForegroundColor Yellow
    $errorsCount++
}

# 2. Check Docker Engine / Desktop
Write-Host "[2/5] Checking Docker..." -NoNewline
try {
    $dockerVer = & docker --version 2>&1
    Write-Host " [OK] ($dockerVer)" -ForegroundColor Green
} catch {
    Write-Host " [WARN] (Docker is not installed or not in PATH)" -ForegroundColor Yellow
    Write-Host "        --> Note: You can still run locally with H2/local MySQL, but Docker is recommended." -ForegroundColor Gray
}

# 3. Check Docker Compose
Write-Host "[3/5] Checking Docker Compose..." -NoNewline
try {
    $composeVer = & docker compose version 2>&1
    Write-Host " [OK] ($composeVer)" -ForegroundColor Green
} catch {
    Write-Host " [WARN] (Docker Compose is not available)" -ForegroundColor Yellow
}

# 4. Check Environment File (.env)
Write-Host "[4/5] Checking Environment Configuration (.env)..." -NoNewline
$rootEnv = Join-Path $PSScriptRoot "..\.env"
$exampleEnv = Join-Path $PSScriptRoot "..\.env.example"

if (Test-Path $rootEnv) {
    Write-Host " [OK] (.env file exists)" -ForegroundColor Green
} else {
    Write-Host " [NOTICE] (.env missing, auto-generating from .env.example...)" -ForegroundColor Yellow
    if (Test-Path $exampleEnv) {
        Copy-Item $exampleEnv $rootEnv
        Write-Host "        --> Created .env from .env.example successfully!" -ForegroundColor Green
    } else {
        Write-Host " [FAIL] (.env.example template missing)" -ForegroundColor Red
        $errorsCount++
    }
}

# 5. Check Port 8080 Availability
Write-Host "[5/5] Checking Port 8080 availability..." -NoNewline
$portInUse = Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue
if ($portInUse) {
    Write-Host " [WARN] (Port 8080 is currently occupied by PID $($portInUse.OwningProcess))" -ForegroundColor Yellow
    Write-Host "        --> You may need to terminate the process or change SERVER_PORT in .env" -ForegroundColor Gray
} else {
    Write-Host " [OK] (Port 8080 is free)" -ForegroundColor Green
}

Write-Host ""
Write-Host "------------------------------------------------------------------" -ForegroundColor Cyan
if ($errorsCount -eq 0) {
    Write-Host "SUCCESS: Pre-flight checks passed! Your environment is ready to run RCMS." -ForegroundColor Green
    Write-Host "To run with Docker:          docker compose up --build" -ForegroundColor White
    Write-Host "To run locally without Docker: .\scripts\run-local.ps1" -ForegroundColor White
} else {
    Write-Host "ERROR: $errorsCount prerequisite check(s) failed. Please fix the issues above." -ForegroundColor Red
}
Write-Host "------------------------------------------------------------------" -ForegroundColor Cyan
