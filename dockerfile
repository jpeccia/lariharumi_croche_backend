# Use a imagem base do OpenJDK
FROM openjdk:17-jdk-slim

# Defina o diretório de trabalho
WORKDIR /app

# Copie o arquivo JAR da aplicação para dentro do contêiner
COPY target/lariharumi-0.0.1-SNAPSHOT.jar /app/app.jar


# Exponha a porta que sua aplicação vai rodar
EXPOSE 8080

# Comando para rodar a aplicação Java Spring
ENTRYPOINT ["java", "-jar", "app.jar"]
