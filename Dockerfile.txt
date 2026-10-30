FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
COPY backend/target/inmobiliaria-backend-1.0.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]