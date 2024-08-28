FROM maven:3.8.7-openjdk-18-slim

COPY ./pom.xml ./pom.xml
RUN mvn dependency:go-offline -B

COPY ./src ./src
RUN mvn package

FROM openjdk:18-alpine

COPY --from=0 ./target/app-jar-with-dependencies.jar app.jar

EXPOSE 8010

CMD ["java","-jar","app.jar"]



