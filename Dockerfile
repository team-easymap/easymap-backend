# 1. Use Amazon Corretto 17 as the base image
FROM amazoncorretto:17

# 2. Set the working directory
WORKDIR /app

# 3. Copy necessary files
COPY gradlew /app/
COPY gradle /app/gradle/
COPY build.gradle /app/
COPY settings.gradle /app/

# 4. Give execution permissions to Gradle wrapper
RUN chmod +x gradlew

# 5. Verify Java version
RUN java -version

# 6. Build the application
RUN ./gradlew build --no-daemon

# 7. Copy the application source code
COPY . .

# 8. Expose port 8080
EXPOSE 8080

# 9. Define the entry point for the container
CMD ["java", "-jar", "build/libs/your-app.jar"]
