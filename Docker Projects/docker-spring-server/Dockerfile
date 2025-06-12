# Use a slim Java JDK base image
FROM eclipse-temurin:20-jdk-jammy

WORKDIR /app

COPY target/docker-spring-server-0.0.1-SNAPSHOT.jar app.jar
COPY spring-server.p12 /app/spring-server.p12
EXPOSE 8443

CMD ["java", "-jar", "app.jar"]