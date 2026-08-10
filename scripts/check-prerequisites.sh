#!/usr/bin/env bash
# ==============================================================================
# RCMS Pre-flight Diagnostic & Environment Check (Linux / macOS)
# ==============================================================================

GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

echo -e "${CYAN}==================================================================${NC}"
echo -e "${CYAN}   RESEARCH CONFERENCE MANAGEMENT SYSTEM (RCMS) - PRE-FLIGHT CHECK${NC}"
echo -e "${CYAN}==================================================================${NC}"
echo ""

ERRORS=0

# 1. Check Java JDK
echo -n "[1/5] Checking Java JDK..."
if command -v java >/dev/null 2>&1; then
    JAVA_VER=$(java -version 2>&1 | head -n 1 | awk -F '"' '{print $2}' | awk -F '.' '{print $1}')
    if [ "$JAVA_VER" -ge 17 ] 2>/dev/null; then
        echo -e " ${GREEN}[OK] (Detected Java $JAVA_VER)${NC}"
    else
        echo -e " ${RED}[FAIL] (Detected Java $JAVA_VER, but JDK 17+ is required)${NC}"
        ERRORS=$((ERRORS+1))
    fi
else
    echo -e " ${RED}[FAIL] (Java is not installed or not in PATH)${NC}"
    echo -e "        --> Install JDK 17+ via your package manager or https://adoptium.net/"
    ERRORS=$((ERRORS+1))
fi

# 2. Check Docker
echo -n "[2/5] Checking Docker..."
if command -v docker >/dev/null 2>&1; then
    DOCKER_VER=$(docker --version)
    echo -e " ${GREEN}[OK] ($DOCKER_VER)${NC}"
else
    echo -e " ${YELLOW}[WARN] (Docker is not installed)${NC}"
fi

# 3. Check Docker Compose
echo -n "[3/5] Checking Docker Compose..."
if docker compose version >/dev/null 2>&1; then
    COMPOSE_VER=$(docker compose version)
    echo -e " ${GREEN}[OK] ($COMPOSE_VER)${NC}"
else
    echo -e " ${YELLOW}[WARN] (Docker Compose is not available)${NC}"
fi

# 4. Check Environment File (.env)
echo -n "[4/5] Checking Environment Configuration (.env)..."
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
ROOT_DIR="$SCRIPT_DIR/.."

if [ -f "$ROOT_DIR/.env" ]; then
    echo -e " ${GREEN}[OK] (.env file exists)${NC}"
else
    if [ -f "$ROOT_DIR/.env.example" ]; then
        cp "$ROOT_DIR/.env.example" "$ROOT_DIR/.env"
        echo -e " ${YELLOW}[NOTICE] (Created .env from .env.example template)${NC}"
    else
        echo -e " ${RED}[FAIL] (.env.example missing)${NC}"
        ERRORS=$((ERRORS+1))
    fi
fi

# 5. Check Port 8080
echo -n "[5/5] Checking Port 8080 availability..."
if lsof -i :8080 >/dev/null 2>&1 || nc -z localhost 8080 >/dev/null 2>&1; then
    echo -e " ${YELLOW}[WARN] (Port 8080 is currently occupied)${NC}"
else
    echo -e " ${GREEN}[OK] (Port 8080 is free)${NC}"
fi

echo ""
echo -e "${CYAN}------------------------------------------------------------------${NC}"
if [ $ERRORS -eq 0 ]; then
    echo -e "${GREEN}SUCCESS: Pre-flight checks passed! Your environment is ready.${NC}"
    echo "To run with Docker:          docker compose up --build"
    echo "To run locally without Docker: ./scripts/run-local.sh"
else
    echo -e "${RED}ERROR: $ERRORS prerequisite check(s) failed. Please resolve above.${NC}"
fi
echo -e "${CYAN}------------------------------------------------------------------${NC}"
