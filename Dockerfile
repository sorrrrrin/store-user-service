# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file from the target folder to the container
COPY target/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
