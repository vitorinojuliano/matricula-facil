FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copia o arquivo JAR da aplicação
COPY target/*.jar app.jar

# Expõe a porta da aplicação
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]