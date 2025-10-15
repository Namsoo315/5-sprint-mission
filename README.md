[![codecov](https://codecov.io/gh/Namsoo315/5-sprint-mission/branch/남현수-sprint8/graph/badge.svg)](https://codecov.io/gh/Namsoo315/5-sprint-mission)

---

````markdown
# Discodeit 프로젝트

이 저장소는 **애플리케이션 컨테이너화 → BinaryContentStorage 고도화 (AWS S3) → AWS 배포 (ECS, RDS) → CI/CD 구축** 의 전체
마일스톤을 포함합니다.

---

## 📌 프로젝트 마일스톤

1. 애플리케이션 컨테이너화
2. BinaryContentStorage 고도화 (AWS S3)
3. AWS를 활용한 배포 (AWS ECS, RDS)
4. CI/CD 파이프라인 구축 (GitHub Actions)

---

## 🚀 애플리케이션 컨테이너화

### Dockerfile 작성

- [x] Amazon Corretto 17 이미지를 베이스 이미지로 사용하세요.
- [x] 작업 디렉토리를 설정하세요. (`/app`)
- [x] 프로젝트 파일을 컨테이너로 복사하세요. (.dockerignore 활용)
- [x] Gradle Wrapper를 사용하여 애플리케이션을 빌드하세요.
- [x] 80 포트를 노출하도록 설정하세요.
- [x] 프로젝트 정보를 환경 변수로 설정하세요.

```bash
PROJECT_NAME=discodeit
PROJECT_VERSION=1.2-M8
````

* [x] JVM 옵션을 환경 변수로 설정하세요.

```bash
JVM_OPTS=""
```

* [x] 환경 변수를 활용하여 애플리케이션 실행 명령어를 설정하세요.

### 이미지 빌드 및 실행 테스트

* [x] Docker 이미지를 빌드하고 태그(local)를 지정하세요.
* [x] 빌드된 이미지를 활용해서 컨테이너를 실행하고 애플리케이션을 테스트하세요.
* [x] prod 프로필로 실행하세요.
* [x] 데이터베이스는 로컬 PostgreSQL을 활용하세요.
* [x] [http://localhost:8081](http://localhost:8081) 접속 가능하도록 포트 매핑하세요.

### Docker Compose 구성

* [x] 애플리케이션과 PostgreSQL 서비스를 포함하세요.
* [x] 환경 변수는 `.env` 파일로 관리하고, `.env`는 git에 포함되지 않도록 합니다.
* [x] 애플리케이션 서비스를 로컬 Dockerfile에서 빌드하도록 구성하세요.
* [x] 애플리케이션 볼륨을 구성하여 BinaryContentStorage 데이터가 유지되도록 하세요.
* [x] PostgreSQL 볼륨을 구성하여 데이터가 유지되도록 하세요.
* [x] PostgreSQL 서비스 실행 후 `schema.sql` 자동 실행되도록 구성하세요.
* [x] 서비스 간 의존성을 설정하세요 (`depends_on`).
* [x] 필요한 포트 매핑을 구성하세요.
* [x] `docker compose up --build`로 서비스 시작 및 테스트하세요.

---

## 📦 BinaryContentStorage 고도화 (AWS S3)

### AWS S3 버킷 구성

* [x] S3 버킷 생성: `discodeit-binary-content-storage-(사용자 이니셜)`
* [x] 퍼블릭 액세스 차단 (모두 차단)
* [x] 버전 관리 비활성화

### IAM 사용자 구성

* [x] `discodeit` 사용자 생성
* [x] AmazonS3FullAccess 권한 부여
* [x] 액세스 키 발급 후 `.env` 파일에 추가

```env
# AWS
AWS_S3_ACCESS_KEY=**엑세스_키**
AWS_S3_SECRET_KEY=**시크릿_키**
AWS_S3_REGION=ap-northeast-2
AWS_S3_BUCKET=버킷_이름
```

### AWS S3 테스트

* [x] SDK 의존성 추가

```gradle
implementation 'software.amazon.awssdk:s3:2.31.7'
```

* [x] `AWSS3Test` 클래스 작성 (업로드/다운로드/PresignedUrl 테스트)
* [x] `.env` 값 로드

### S3 BinaryContentStorage 구현

* [x] `S3BinaryContentStorage` 구현
* [x] `@ConditionalOnProperty` 로 `discodeit.storage.type=s3`일 때만 Bean 등록
* [x] PresignedUrl을 활용한 리다이렉트 방식 `download()` 구현
* [x] `S3BinaryContentStorageTest` 작성

---

## ☁️ AWS를 활용한 배포 (RDS, ECR, ECS)

### AWS RDS

* [x] PostgreSQL RDS 인스턴스 생성 (프리 티어, 퍼블릭 액세스 비활성화)
* [x] EC2 생성 후 SSH 터널링을 통해 RDS 접근
* [x] DataGrip으로 접속해 유저/DB/schema 초기화

```sql
CREATE USER discodeit_user WITH PASSWORD 'discodeit1234';
GRANT discodeit_user TO postgres;
CREATE
DATABASE discodeit OWNER discodeit_user;
-- schema.sql 실행
```

### AWS ECR

* [x] 퍼블릭 레포지토리 `discodeit` 생성
* [x] Docker 이미지 빌드 후 push (`latest`, `1.2-M8`)
* [x] 멀티 플랫폼: linux/amd64, linux/arm64

### AWS ECS

* [x] 환경 변수 파일 `discodeit.env` 작성 후 S3 업로드
* [x] ECS 클러스터, 태스크 정의, 서비스 생성
* [x] EC2 보안 그룹 인바운드 규칙 (HTTP Anywhere-IPv4 허용)
* [x] ECS 태스크 실행 후 EC2 퍼블릭 IP 접속 확인

---

## 🔧 심화 요구사항

### 이미지 최적화

* [x] 멀티 스테이지 빌드 (`local-slim` 태그)
* [x] 이미지 크기 비교

### CI/CD (GitHub Actions)

* [x] `.github/workflows/test.yml` 작성 (PR 시 테스트 실행)
* [x] CodeCov 연동 및 커버리지 뱃지 추가
* [x] `.github/workflows/deploy.yml` 작성 (release 브랜치 push 시 배포)
* [x] GitHub Secrets & Variables 설정
* [x] Docker 이미지 빌드 및 ECR push
* [x] ECS 서비스 업데이트 자동화

---

## 📖 참고

* 프리티어 과금 주의 (EC2, RDS, 모니터링 옵션 등)
* `.env` 파일은 반드시 git에 포함하지 않습니다.
