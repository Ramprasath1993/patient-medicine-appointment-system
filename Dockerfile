# Stage 1: Build the Spring Boot application
# Use Maven and OpenJDK 17 to build the project
FROM maven:3.8.8-openjdk-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and download dependencies first to leverage Docker cache
COPY pom.xml .
# Download dependencies for faster subsequent builds if pom.xml doesn't change
RUN mvn dependency:go-offline

# Copy the entire project source code
COPY src /app/src

# Package the application into a JAR file, skipping tests
# The output JAR will be in target/patient-medicine-appointment-system-0.0.1-SNAPSHOT.jar
RUN mvn clean package -DskipTests

# Stage 2: Run the Spring Boot application
# Use a smaller base image for the final runtime
FROM openjdk:17-jdk-slim

# Copy the JAR file from the build stage into the final image
# The wildcard (*) is used to handle the exact JAR name and version.
# Ensure 'app.jar' is the name of the file inside the Docker image.
COPY --from=build /app/target/*.jar app.jar

# Expose the port that Spring Boot will listen on inside the container.
# Your Spring Boot app is configured for 8081 in application.properties.
# Render will map its internal container port (e.g., 8081) to its public HTTPS port (443).
EXPOSE 8081

# Command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "/app.jar"]
