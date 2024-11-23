# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim AS build

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the local machine to the container
COPY target/backend-assignment-sandbox-0.0.1-SNAPSHOT.jar /app/backend-assignment-sandbox-0.0.1-SNAPSHOT.jar

# Expose the port that your application will run on
EXPOSE 8080

# Define the command to run the JAR file
CMD ["java", "-jar", "/app/backend-assignment-sandbox-0.0.1-SNAPSHOT.jar"]

