# Use a slim Java JDK base image
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY target/docker-spring-server-0.0.1-SNAPSHOT.jar app.jar
COPY spring-server.p12 /app/spring-server.p12
RUN ls -l /app/
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]