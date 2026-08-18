
FROM eclipse-temurin:17-jre-alpine
WORKDIR /workspace

# SECURITY: Run as a non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy the Fat Jar
COPY target/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

