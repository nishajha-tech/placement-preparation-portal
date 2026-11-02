# Build Stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
# Download dependencies
RUN mvn dependency:go-offline -B
COPY src ./src
# Build the application jar
RUN mvn clean package -DskipTests

# Runtime Stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/placement-preparation-portal-1.0.0.jar app.jar

ENV PORT=8088
EXPOSE 8088

ENTRYPOINT ["java", "-Dspring.profiles.active=h2", "-jar", "app.jar"]
