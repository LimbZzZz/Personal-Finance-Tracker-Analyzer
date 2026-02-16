@echo off
chcp 65001 > nul
echo 1. Собираем проект...
call mvnw clean package -DskipTests

echo.
echo 2. Запускаем Docker...
docker compose up -d --build

echo.
echo 3. Проверяем логи...
docker compose logs -f spring_app

pause