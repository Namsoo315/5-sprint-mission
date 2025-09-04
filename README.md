# Discodeit Backend

스프린트 미션: **Discodeit** 백엔드 서버 구현

---

## 기본 요구사항

### API 명세
이번 미션은 아래의 API 스펙과 비교하며 구현해보세요.

- **API 스펙 v1.1**
- API 스펙을 준수한다면, 아래의 프론트엔드 코드와 호환됩니다.
- 정적 리소스 v1.1.4  
- 소스 코드(참고용) v1.1.4  
- 프론트엔드 소스 코드는 참고용으로만 활용하세요.  
  수정하여 활용하는 경우 이어지는 요구사항 또는 미션을 수행하는 데 어려움이 있을 수 있습니다.

---

## 데이터베이스

✅ 아래와 같이 데이터베이스 환경을 설정하세요.  
- 데이터베이스: `discodeit`  
- 유저: `discodeit_user`  
- 패스워드: `discodeit1234`  

✅ ERD를 참고하여 DDL을 작성하고, 테이블을 생성하세요.  
- 작성한 DDL 파일은 `/src/main/resources/schema.sql` 경로에 포함하세요.  

제약조건:  
- PK: Primary Key  
- UK: Unique Key  
- NN: Not Null  
- FK: Foreign Key  
- ON DELETE CASCADE: 연관 엔티티 삭제 시 같이 삭제  
- ON DELETE SET NULL: 연관 엔티티 삭제 시 NULL로 변경  

---

## Spring Data JPA 적용하기

✅ Spring Data JPA와 PostgreSQL을 위한 의존성을 추가하세요.  
✅ 앞서 구성한 데이터베이스에 연결하기 위한 설정값을 `application.yaml` 파일에 작성하세요.  
✅ 디버깅을 위해 SQL 로그와 관련된 설정값을 `application.yaml` 파일에 작성하세요.  

---

## 엔티티 정의하기

✅ 클래스 다이어그램을 참고해 도메인 모델의 공통 속성을 추상 클래스로 정의하고 상속 관계를 구현하세요.  
- 이때 `Serializable` 인터페이스는 제외합니다.  
- 패키지명: `com.sprint.mission.discodeit.entity.base`  

✅ JPA의 어노테이션을 활용해 `createdAt`, `updatedAt` 속성이 자동으로 설정되도록 구현하세요.  
- `@CreatedDate`, `@LastModifiedDate`  

✅ 클래스 다이어그램을 참고해 클래스 참조 관계를 수정하세요.  
- 필요한 경우 생성자, update 메소드를 수정할 수 있습니다.  
- 단, 아직 JPA Entity와 관련된 어노테이션은 작성하지 마세요.  

✅ ERD와 클래스 다이어그램을 토대로 연관관계 매핑 정보를 표로 정리해보세요.  

| A       | B             | 다중성 | 방향성                                | 부모-자식 관계             | 연관관계의 주인 |
|---------|---------------|--------|--------------------------------------|----------------------------|----------------|
| User    | UserStatus    | 1:1    | 단방향 User → UserStatus              | 부모: User, 자식: UserStatus | User |
| User    | ReadStatus    | 1:N    | 단방향 ReadStatus → User              | 부모: User, 자식: ReadStatus | ReadStatus |
| User    | Message       | 1:N    | 단방향 Message → User (author)        | 부모: User, 자식: Message   | Message |
| User    | BinaryContent | 1:1(*) | 단방향 User → BinaryContent (profile) | 부모: User, 자식: BinaryContent | User |
| Channel | ReadStatus    | 1:N    | 단방향 ReadStatus → Channel           | 부모: Channel, 자식: ReadStatus | ReadStatus |
| Channel | Message       | 1:N    | 단방향 Message → Channel              | 부모: Channel, 자식: Message | Message |
| Message | BinaryContent | 1:N    | 단방향 Message → BinaryContent (files)| 부모: Message, 자식: BinaryContent | Message |

✅ JPA 주요 어노테이션을 활용해 ERD, 연관관계 매핑 정보를 도메인 모델에 반영해보세요.  
- `@Entity`, `@Table`  
- `@Column`, `@Enumerated`  
- `@OneToMany`, `@OneToOne`, `@ManyToOne`  
- `@JoinColumn`, `@JoinTable`  

✅ ERD의 외래키 제약 조건과 연관관계 매핑 정보의 부모-자식 관계를 고려해 영속성 전이와 고아 객체를 정의하세요.  
- `cascade`, `orphanRemoval`  

---

## 레포지토리와 서비스에 JPA 도입하기

✅ 기존의 Repository 인터페이스를 `JpaRepository`로 정의하고 쿼리 메소드로 대체하세요.  
✅ `FileRepository`와 `JCFRepository` 구현체는 삭제합니다.  
✅ 영속성 컨텍스트의 특징에 맞추어 서비스 레이어를 수정해보세요.  
- 힌트: 트랜잭션, 영속성 전이, 변경 감지, 지연 로딩  

---

## DTO 적극 도입하기

✅ Entity를 Controller까지 그대로 노출했을 때 발생할 수 있는 문제점에 대해 정리해보세요.  
✅ DTO를 적극 도입했을 때의 장점을 파악하세요.  
- Entity와 API의 결합 방지  
- 프로덕션 환경(OSIV = false) 대응  
- 양방향 연관관계 시 순환 참조 방지  
- 민감 데이터 보호  

✅ 클래스 다이어그램을 참고하여 DTO를 정의하세요.  
✅ Entity를 DTO로 매핑하는 로직을 책임지는 Mapper 컴포넌트를 정의해 반복되는 코드를 줄여보세요.  
- 패키지명: `com.sprint.mission.discodeit.mapper`  

---

## BinaryContent 저장 로직 고도화

✅ BinaryContent 엔티티는 파일의 메타 정보(`fileName`, `size`, `contentType`)만 표현하도록 `bytes` 속성을 제거하세요.  
✅ BinaryContent의 `byte[]` 데이터 저장을 담당하는 인터페이스를 설계하세요.  
- 패키지명: `com.sprint.mission.discodeit.storage`  

### BinaryContentStorage
- `UUID put(UUID, byte[])` : 키(UUID) 기반 저장  
- `InputStream get(UUID)` : 키 기반 조회  
- `ResponseEntity<?> download(BinaryContentDto)` : 다운로드 응답 생성  

✅ 서비스 레이어에서 기존 BinaryContent 저장 로직을 BinaryContentStorage로 리팩토링하세요.  
✅ BinaryContentController에 파일 다운로드 API를 추가하고, BinaryContentStorage에 로직을 위임하세요.  
- 엔드포인트: `GET /api/binaryContents/{binaryContentId}/download`  
- 요청: `binaryContentId` (Query Parameter)  
- 응답: `ResponseEntity<?>`  

✅ 로컬 디스크 저장 방식 구현체를 작성하세요.  
- `discodeit.storage.type = local` 일 때만 Bean 등록  
- `discodeit.storage.local.root-path` 설정값 기반 저장  
- 파일 저장 규칙: `{root}/{UUID}`  
- `init()` 시 디렉토리 초기화  
- `resolvePath(UUID)` 로 일관된 경로 유지  
- `download()` 에서 ResponseEntity<Resource> 생성  

---

## 페이징과 정렬

✅ 메시지 목록 조회 시 조건:  
- **50개씩**  
- **최신순**  
- **총 개수 불필요**  

✅ 일관된 페이지네이션 응답을 위해 제네릭 DTO 구현  
- 패키지명: `com.sprint.mission.discodeit.dto.response`  

구조:  
- `content` : 실제 데이터  
- `number` : 페이지 번호  
- `size` : 페이지 크기  
- `totalElements` : 총 개수 (nullable)  

✅ `Slice` 또는 `Page` 객체로부터 DTO를 생성하는 Mapper 구현  
- 패키지명: `com.sprint.mission.discodeit.mapper`  
- 확장성을 위해 제네릭 메소드로 작성  

---
