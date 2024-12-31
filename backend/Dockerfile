# Desenvolvimento
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY pom.xml .
COPY ./src ./src

# Instalar Maven
RUN apt-get update && apt-get install -y maven

# Expor porta do Spring e DevTools
EXPOSE 8080
EXPOSE 35729

# Comando para desenvolvimento
CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000'"]