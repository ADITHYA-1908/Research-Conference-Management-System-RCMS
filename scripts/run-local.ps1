# ==============================================================================
# RCMS Local Developer Launcher (Windows PowerShell)
# ==============================================================================

Write-Host "[INFO] Starting Research Conference Management System locally..." -ForegroundColor Cyan

$rootDir = Join-Path $PSScriptRoot ".."
Set-Location (Join-Path $rootDir "rcms")

# Ensure .env exists at root
$envFile = Join-Path $rootDir ".env"
$envExample = Join-Path $rootDir ".env.example"
if (-not (Test-Path $envFile) -and (Test-Path $envExample)) {
    Copy-Item $envExample $envFile
    Write-Host "[INFO] Automatically created root .env configuration file." -ForegroundColor Yellow
}

# Execute Spring Boot using Maven wrapper or Maven
if (Test-Path ".\mvnw.cmd") {
    & ".\mvnw.cmd" spring-boot:run
} elseif (Test-Path "..\apache-maven-3.9.9\bin\mvn.cmd") {
    & "..\apache-maven-3.9.9\bin\mvn.cmd" spring-boot:run
} else {
    & mvn spring-boot:run
}
