@echo off
title Placement Preparation Portal (MySQL Mode)
echo ===================================================================
echo   Placement Preparation Portal - MySQL Server Mode
echo   Connecting to:   jdbc:mysql://localhost:3306/placement_portal
echo   Open in browser: http://localhost:8088
echo   Demo Student:    rahul@student.com / student123
echo   Demo Admin:      admin@portal.com / admin123
echo ===================================================================
echo.
cd /d "%~dp0"
start http://localhost:8088
"C:\Program Files\JetBrains\IntelliJ IDEA 2025.3.1.1\plugins\maven\lib\maven3\bin\mvn.cmd" spring-boot:run "-Dspring-boot.run.profiles=mysql"
pause
