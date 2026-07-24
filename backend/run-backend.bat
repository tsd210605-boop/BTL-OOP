@echo off
REM Thiết lập JAVA_HOME cho JDK 17 để tránh lỗi tương thích Lombok với Java 25
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.18.8-hotspot
set PATH=%JAVA_HOME%\bin;%PATH%

echo Dang su dung JAVA_HOME: %JAVA_HOME%
mvn spring-boot:run
