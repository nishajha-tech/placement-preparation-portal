@echo off
title Placement Preparation Portal (H2 In-Memory Mode)
echo ===================================================================
echo   Placement Preparation Portal - Instant Zero-Config Mode (H2 DB)
echo   Open in browser: http://localhost:8088
echo   H2 Console:      http://localhost:8088/h2-console
echo   Demo Student:    rahul@student.com / student123
echo   Demo Admin:      admin@portal.com / admin123
echo ===================================================================
echo.
cd /d "%~dp0"
start http://localhost:8088
"C:\Program Files\JetBrains\IntelliJ IDEA 2025.3.1.1\plugins\maven\lib\maven3\bin\mvn.cmd" spring-boot:run "-Dspring-boot.run.profiles=h2"
pause
