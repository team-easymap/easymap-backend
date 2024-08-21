# 1. Build stage
FROM eclipse-temurin:17-jdk-slim AS build

# 2. Set working directory
WORKDIR /app

# 3. Copy Gradle wrapper and build files
COPY gradlew /app/
COPY gradle /app/gradle/
COPY build.gradle /app/
COPY settings.gradle /app/

# 4. Download Gradle dependencies
RUN chmod +x gradlew
RUN ./gradlew build --no-daemon

# 5. Copy the application source code
COPY src /app/src

# 6. Build the application
RUN ./gradlew build -x test --no-daemon

# 7. Create a minimal runtime image
FROM eclipse-temurin:17-jre-slim

# 8. Set working directory
WORKDIR /app

# 9. Copy the built JAR file from the build stage
COPY --from=build /app/build/libs/*.jar /app/app.jar

# 10. Specify the command to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

# 11. Expose the port the application runs on
EXPOSE 8080
