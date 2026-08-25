FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/order-consumer-service-*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]
