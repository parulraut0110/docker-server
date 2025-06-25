FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY target/docker-kafka-spring-python-0.0.1-SNAPSHOT.jar app.jar
COPY spring-server.p12 /app/spring-server.p12
COPY spring-server.crt /app/spring-server.crt

RUN keytool -importcert -noprompt \
      -trustcacerts \
      -alias spring-server.crt \
      -file /app/spring-server.crt \
      -keystore $JAVA_HOME/lib/security/cacerts \
      -storepass changeit

EXPOSE 8443

CMD ["java", "-jar", "app.jar"]