# syntax=docker/dockerfile:1
FROM maven:3.8.7-openjdk-18-slim AS build-stage

COPY ./pom.xml ./pom.xml
RUN mvn dependency:go-offline -B

COPY ./src ./src
RUN mvn package

FROM openjdk:18-alpine

COPY --from=build-stage ./target/app-jar-with-dependencies.jar app.jar

# act as doc only
EXPOSE 8010
LABEL vendor=wastingnotime.org

CMD ["java","-jar","app.jar"]



