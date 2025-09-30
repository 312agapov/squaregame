# Сборка консольного приложения
FROM gradle:8.7.0-jdk21 AS builder

WORKDIR /app
COPY . .

# Собираем только console-app
RUN gradle :console-app:shadowJar --no-daemon

# Минимальный образ для запуска
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app
COPY --from=builder /app/console-app/build/libs/*-all.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
