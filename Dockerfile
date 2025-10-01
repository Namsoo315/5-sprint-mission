# [1] Amazon Corretto 17 이미지를 베이스 이미지로 사용
FROM amazoncorretto:17 AS build

# [2] 작업 디렉토리 설정
WORKDIR /app

# [3] Gradle Wrapper 및 프로젝트 파일 복사
COPY . .

# [4] Gradle Wrapper를 이용해 애플리케이션 빌드 (bootJar 생성)
RUN ./gradlew clean bootJar --no-daemon

# ----------- 실제 실행 환경 -----------
FROM amazoncorretto:17

# [2] 실행 환경 작업 디렉토리
WORKDIR /app

# [5] 빌드된 jar만 복사
COPY --from=build /app/build/libs/*.jar /app/

# [6] 80 포트 노출
EXPOSE 80

# [7] 프로젝트 정보 환경 변수
ENV PROJECT_NAME=discodeit \
    PROJECT_VERSION=1.2-M8 \
    JVM_OPTS=""

# [8] 애플리케이션 실행 (prod 프로필)
ENTRYPOINT ["sh", "-c", "java $JVM_OPTS -jar /app/${PROJECT_NAME}-${PROJECT_VERSION}.jar --spring.profiles.active=prod"]

# 실행 명령어
# > docker build -t discodeit:local .
# > docker run -d --name discodeit-app -p 8081:80 discodeit:local
# > docker build --no-cache -t discodeit:local . 재 빌드