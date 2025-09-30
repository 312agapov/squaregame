Запуск консольного приложения:
docker compose -f docker-compose-console.yml up -d
docker exec -it square-console sh -c "java -jar app.jar"