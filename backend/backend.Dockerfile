FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/healthcare-appointment-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 2025
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

