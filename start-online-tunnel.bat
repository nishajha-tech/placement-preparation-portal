@echo off
title Placement Preparation Portal - Cloudflare Public Tunnel
echo ===================================================================
echo    Placement Preparation Portal - Cloudflare Public Tunnel
echo    Exposing Spring Boot (Port 8088) to the Public Internet
echo ===================================================================
echo.
cd /d "%~dp0"

:: 1. Check if Spring Boot portal is running on port 8088
netstat -ano | findstr ":8088 " | findstr "LISTENING" >nul
if errorlevel 1 (
    echo [INFO] Spring Boot server is not running on port 8088.
    echo [INFO] Starting Spring Boot server in background now...
    start "PlacementPortal-Backend" cmd /c "call run-h2.bat"
    echo Waiting for server to initialize on port 8088...
    timeout /t 10 /nobreak >nul
) else (
    echo [OK] Spring Boot server is already active and listening on port 8088.
)

:: 2. Check for cloudflared executable
if exist "..\cloudflared.exe" (
    echo.
    echo ===================================================================
    echo  Starting Public Tunnel for http://localhost:8088 ...
    echo  Your active public URL will appear below:
    echo ===================================================================
    echo.
    ..\cloudflared.exe tunnel --edge-ip-version 4 --protocol http2 --url http://localhost:8088
) else (
    echo [ERROR] cloudflared.exe not found in parent directory.
)
pause
