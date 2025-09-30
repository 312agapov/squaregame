# Сборка Spring Boot веб-сервиса
FROM gradle:8.7.0-jdk21 AS builder

WORKDIR /app
COPY . .

# Собираем только web-service
RUN gradle :web-service:bootJar --no-daemon

# Минимальный образ для запуска
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app
COPY --from=builder /app/web-service/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
