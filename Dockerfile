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

# 7. Replace secrets in application.yml
COPY src/main/resources/application.yml /app/src/main/resources/application.yml
RUN sed -i -e "s#{{GOOGLE_CLIENT_ID}}#${GOOGLE_CLIENT_ID}#g" /app/src/main/resources/application.yml && \
    sed -i -e "s#{{GOOGLE_CLIENT_SECRET}}#${GOOGLE_CLIENT_SECRET}#g" /app/src/main/resources/application.yml && \
    sed -i -e "s#{{KAKAO_CLIEND_ID}}#${KAKAO_CLIEND_ID}#g" /app/src/main/resources/application.yml && \
    sed -i -e "s#{{KAKAO_CLIENT_SECRET}}#${KAKAO_CLIENT_SECRET}#g" /app/src/main/resources/application.yml && \
    sed -i -e "s#{{DATASOURCE_URL}}#${DATASOURCE_URL}#g" /app/src/main/resources/application.yml && \
    sed -i -e "s#{{DB_USERNAME}}#${DB_USERNAME}#g" /app/src/main/resources/application.yml && \
    sed -i -e "s#{{DB_PASSWORD}}#${DB_PASSWORD}#g" /app/src/main/resources/application.yml && \
    sed -i -e "s#{{JWT_SECRET}}#${JWT_SECRET}#g" /app/src/main/resources/application.yml && \
    sed -i -e "s#{{REDIRECT_URL}}#${REDIRECT_URL}#g" /app/src/main/resources/application.yml && \
    sed -i -e "s#{{REDIRECT_URL_FAILED}}#${REDIRECT_URL_FAILED}#g" /app/src/main/resources/application.yml && \
    sed -i -e "s#{{AWS_ACCESS_KEY}}#${AWS_ACCESS_KEY}#g" /app/src/main/resources/application.yml && \
    sed -i -e "s#{{AWS_SECRET_ACCESS_KEY}}#${AWS_SECRET_ACCESS_KEY}#g" /app/src/main/resources/application.yml && \
    sed -i -e "s#{{AWS_IMAGE_BUCKET_NAME}}#${AWS_IMAGE_BUCKET_NAME}#g" /app/src/main/resources/application.yml && \
    sed -i -e "s#{{AWS_USER_RAWDATA_BUCKET_NAME}}#${AWS_USER_RAWDATA_BUCKET_NAME}#g" /app/src/main/resources/application.yml && \
    sed -i -e "s#{{VWORLD_CONFORM_KEY}}#${VWORLD_CONFORM_KEY}#g" /app/src/main/resources/application.yml && \
    sed -i -e "s#{{JUSO_CONFORM_KEY}}#${JUSO_CONFORM_KEY}#g" /app/src/main/resources/application.yml

# 8. Build the application
RUN ./gradlew build --no-daemon

# 9. Run the application
CMD ["java", "-jar", "build/libs/easymap-0.0.1-SNAPSHOT.jar"]
