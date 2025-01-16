# Etapa 1: Build
FROM maven:3.8.7-eclipse-temurin-17 AS build
WORKDIR /app

# Copie os arquivos necessários para o build
COPY pom.xml .
COPY src ./src

# Execute o build do JAR
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copie o JAR gerado na etapa de build
COPY --from=build /app/target/lariharumi-0.0.1-SNAPSHOT.jar app.jar

# Exponha a porta usada pelo Spring Boot
EXPOSE 8080

# Comando para executar o JAR
CMD ["java", "-jar", "app.jar"]
