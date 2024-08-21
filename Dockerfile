# 1. Build stage
FROM openjdk:17-oracle AS build

# 2. Set working directory
WORKDIR /app

# 3. Install necessary tools (including xargs) using microdnf
RUN microdnf install findutils

# 4. Copy Gradle wrapper and build files
COPY gradlew /app/
COPY gradle /app/gradle/
COPY build.gradle /app/
COPY settings.gradle /app/

# 5. Download Gradle dependencies
RUN chmod +x gradlew
RUN ./gradlew build --no-daemon

# 6. Copy the application source code
COPY src /app/src

# 7. Build the application
RUN ./gradlew build -x test --no-daemon

# 8. Create a minimal runtime image
FROM openjdk:17-oracle

# 9. Set working directory
WORKDIR /app

# 10. Copy the built JAR file from the build stage
COPY --from=build /app/build/libs/*.jar /app/app.jar

# 11. Specify the command to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

# 12. Expose the port the application runs on
EXPOSE 8080