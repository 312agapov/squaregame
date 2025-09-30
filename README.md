## **Запуск консольного приложения:**
``` docker compose -f docker-compose-console.yml up -d ``` <br>
``` docker exec -it square-console sh -c "java -jar app.jar" ```

## **Запуск веб-приложения:**
``` docker compose -f docker-compose-web.yml up -d ```