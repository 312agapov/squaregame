Запуск веб-приложения:
docker compose -f docker-compose-console.yml up -d
docker exec -it console sh -c "java -jar app.jar"