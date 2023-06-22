# syntax=docker/dockerfile:1
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /patient_ui

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN ./mvnw dependency:resolve

COPY src ./src

CMD ["./mvnw", "spring-boot:run"]