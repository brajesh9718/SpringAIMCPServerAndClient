FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/order-producer-service-*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]
