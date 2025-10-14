# 빌드 스테이지
FROM amazoncorretto:17 AS builder

WORKDIR /app

COPY gradlew ./
RUN chmod +x gradlew
COPY gradle ./gradle
COPY settings.gradle ./settings.gradle
COPY build.gradle ./build.gradle
RUN ./gradlew dependencies

COPY src ./src
RUN ./gradlew clean bootJar -x test --no-daemon

# 런타임 스테이지
FROM amazoncorretto:17

WORKDIR /app

ENV SPRING_PROFILES_ACTIVE=prod \
    PROJECT_NAME=discodeit \
    PROJECT_VERSION=1.2-M8 \
    JVM_OPTS="" \
    SERVER_PORT=80

COPY --from=builder /app/build/libs/*.jar /app/

RUN set -eux; \
    JAR_PATH="$(ls /app/*.jar | head -n1)"; \
    TARGET_PATH="/app/${PROJECT_NAME}-${PROJECT_VERSION}.jar"; \
    if [ "$JAR_PATH" != "$TARGET_PATH" ]; then mv "$JAR_PATH" "$TARGET_PATH"; fi

EXPOSE 80

ENTRYPOINT ["sh", "-c", "java $JVM_OPTS -Dserver.port=${SERVER_PORT} -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -jar /app/${PROJECT_NAME}-${PROJECT_VERSION}.jar"]
