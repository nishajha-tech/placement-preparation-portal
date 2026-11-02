@echo off
title Placement Preparation Portal - Ngrok Static Domain Tunnel
echo ===================================================================
echo    Placement Preparation Portal - Ngrok Static Domain Tunnel
echo    Exposing Port 8088 using a Permanent / Static Domain
echo ===================================================================
echo.
echo Free Ngrok account provides 1 Free Static Domain that never changes.
echo Example usage:
echo   ngrok http 8088 --domain your-domain.ngrok-free.app
echo.
set /p DOMAIN="Enter your Ngrok Static Domain (or press Enter for dynamic): "

if "%DOMAIN%"=="" (
    ngrok http 8088
) else (
    ngrok http 8088 --domain %DOMAIN%
)
pause
