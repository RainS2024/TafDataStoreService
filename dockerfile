
FROM amazoncorretto:17
WORKDIR /app
COPY build/libs/DataStore-service-v1.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
