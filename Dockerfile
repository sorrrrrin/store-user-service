# Use a reliable Eclipse Temurin OpenJDK 17 runtime as a parent image
FROM eclipse-temurin:17-jdk-jammy

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file from the target folder to the container
COPY target/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:InitialRAMPercentage=30.0", "-XX:MaxRAMPercentage=60.0", "-XX:+ExitOnOutOfMemoryError", "-XX:+UseG1GC", "-jar", "app.jar"]
