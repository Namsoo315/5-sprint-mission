## ========== 빌드 스테이지 ==========
#FROM amazoncorretto:17 AS builder
#
#WORKDIR /app
#
## gradle 관련 파일만 먼저 복사 (캐시 활용)
#COPY gradlew ./
#COPY gradle ./gradle
#COPY build.gradle settings.gradle ./
#RUN chmod +x gradlew && ./gradlew dependencies --no-daemon
#
## 나중에 소스 복사 (변경이 잦음)
#COPY src ./src
#RUN ./gradlew clean bootJar -x test --no-daemon
#
#
## ========== 런타임 스테이지 (슬림) ==========
#FROM amazoncorretto:17-alpine AS runtime
#
#WORKDIR /app
#
## 런타임에 필요한 최소 도구만 설치
#RUN apk add --no-cache curl
#
#ENV SPRING_PROFILES_ACTIVE=prod \
#    PROJECT_NAME=discodeit \
#    PROJECT_VERSION=1.2-M8 \
#    JVM_OPTS="" \
#    SERVER_PORT=80
#
## 빌드 결과물만 복사
#COPY --from=builder /app/build/libs/*.jar /app/app.jar
#
#EXPOSE 80
#
#ENTRYPOINT ["sh", "-c", "java $JVM_OPTS -Dserver.port=${SERVER_PORT} -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -jar /app/app.jar"]
#

###############################################################
#       위에는 Slim file
#       밑에는 기존 file
###############################################################

# 빌드 스테이지
FROM amazoncorretto:17 AS builder

WORKDIR /app

COPY gradlew ./gradlew
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

# curl 설치 (yum 기반)
RUN yum update -y && yum install -y curl && yum clean all

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
