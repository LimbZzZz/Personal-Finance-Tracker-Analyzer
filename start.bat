@echo off
chcp 65001 > nul
call mvnw clean package -DskipTests

echo.
docker compose up -d --build

echo.
docker compose logs -f spring_app

pause