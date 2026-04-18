# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copiar el wrapper y el pom
COPY mvnw pom.xml ./
COPY .mvn .mvn/
RUN chmod +x mvnw || true

# Descargar dependencias
RUN ./mvnw dependency:go-offline -B || mvn dependency:go-offline -B

# Copiar el código y construir
COPY src ./src
RUN ./mvnw clean package -DskipTests || mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Crear usuario para no correr como root
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
