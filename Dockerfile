# Use the official OpenJDK image as a parent image
FROM eclipse-temurin:17-jdk as build

# Set the working directory
WORKDIR /app
# Copy the Gradle wrapper files
COPY gradlew .
COPY gradle gradle

# Copy the build configuration files
COPY build.gradle .
COPY settings.gradle .

# Copy the project files
COPY src src

# Copy env
COPY .env .
# Export env to global env
COPY export-env.sh .
RUN chmod +x export-env.sh
RUN source export-env.sh

# Make the gradlew file executable
RUN chmod +x gradlew

# Build the application
RUN ./gradlew bootJar --no-daemon

# Create a new stage with minimal runtime dependencies
FROM eclipse-temurin:17-jre

# Set working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the port the app runs on
EXPOSE 8888

# Set JVM options for containerized environments
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseContainerSupport"

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]