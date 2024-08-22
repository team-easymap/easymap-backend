# 1. Use Amazon Corretto as base image
FROM amazoncorretto:17

# 2. Set working directory
WORKDIR /app

# 3. Copy gradle wrapper and gradle files
COPY gradlew /app/
COPY gradle /app/gradle/

# 4. Copy gradle build files
COPY build.gradle /app/
COPY settings.gradle /app/

# 5. Make gradlew executable
RUN chmod +x gradlew

# 6. Copy the application source code
COPY src /app/src

# 8. Build the application
RUN ./gradlew build --no-daemon

# 9. Run the application
CMD ["java", "-jar", "build/libs/easymap-0.0.1-SNAPSHOT.jar"]
