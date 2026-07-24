@echo off
echo Stopping old processes...

:: Tắt port 8081 nếu đang chạy
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8081') do taskkill /PID %%a /F 2>nul

:: Tắt port 3000 nếu đang chạy
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :3000') do taskkill /PID %%a /F 2>nul

echo Starting Backend...
start cmd /k "cd /d D:\NetBeansProjects\workreport && mvn spring-boot:run"

timeout /t 10

echo Starting Frontend...
start cmd /k "cd /d D:\NetBeansProjects\workreport\frontend && npm start"