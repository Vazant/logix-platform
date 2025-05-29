FROM eclipse-temurin:21-jdk-alpine

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранный jar-файл (убедись, что JAR один в папке target/)
COPY target/logix-currency-*.jar app.jar

# Порт, который слушает сервис
EXPOSE 8081

# Запуск приложения
ENTRYPOINT ["java", "-jar", "app.jar"]
