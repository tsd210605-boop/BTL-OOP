@echo off
title HE THONG QUAN LY CONG VIEC VA BAI TAP - JAVA DESKTOP SWING (OOP)
REM Thiết lập JAVA_HOME cho JDK 17 để đảm bảo tương thích tốt nhất
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.18.8-hotspot
set PATH=%JAVA_HOME%\bin;%PATH%

echo =========================================================================
echo KHOI DONG UNG DUNG JAVA DESKTOP SWING (OOP MVC + MYSQL)
echo Dang su dung JAVA_HOME: %JAVA_HOME%
echo =========================================================================
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Djava.awt.headless=false -Dserver.port=0"
