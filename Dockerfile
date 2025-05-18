# Use a JDK base image
FROM openjdk:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the built jar file (adjust the name if needed)
COPY target/privacybox-0.0.1-SNAPSHOT.jar app.jar

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]