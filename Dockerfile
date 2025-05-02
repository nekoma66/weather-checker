FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src/ ./src/
RUN mvn clean install

FROM amazoncorretto:17-alpine AS prod
RUN mkdir /app
COPY --from=builder /app/target/*.jar /app.jar
ENV SERVER_PORT=8080
WORKDIR /app
EXPOSE 8080
RUN ls
ENTRYPOINT ["java","-jar","/app.jar"]