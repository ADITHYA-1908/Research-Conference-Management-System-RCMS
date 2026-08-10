#!/usr/bin/env bash
# ==============================================================================
# RCMS Local Developer Launcher (Linux / macOS)
# ==============================================================================

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
ROOT_DIR="$SCRIPT_DIR/.."

echo "[INFO] Navigating to rcms application directory..."
cd "$ROOT_DIR/rcms" || exit 1

if [ ! -f "$ROOT_DIR/.env" ] && [ -f "$ROOT_DIR/.env.example" ]; then
    cp "$ROOT_DIR/.env.example" "$ROOT_DIR/.env"
    echo "[INFO] Generated root .env configuration file."
fi

echo "[INFO] Launching RCMS via Maven..."
if [ -f "./mvnw" ]; then
    chmod +x ./mvnw
    ./mvnw spring-boot:run
else
    mvn spring-boot:run
fi
