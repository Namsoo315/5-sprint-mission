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

- [x] 아래와 같이 데이터베이스 환경을 설정하세요.  
  - 데이터베이스: `discodeit`  
  - 유저: `discodeit_user`  
  - 패스워드: `discodeit1234`  

- [x] ERD를 참고하여 DDL을 작성하고, 테이블을 생성하세요.  
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

- [x] Spring Data JPA와 PostgreSQL을 위한 의존성을 추가하세요.  
- [x] 앞서 구성한 데이터베이스에 연결하기 위한 설정값을 `application.yaml` 파일에 작성하세요.  
- [x] 디버깅을 위해 SQL 로그와 관련된 설정값을 `application.yaml` 파일에 작성하세요.  

---

## 엔티티 정의하기

- [x] 클래스 다이어그램을 참고해 도메인 모델의 공통 속성을 추상 클래스로 정의하고 상속 관계를 구현하세요.  
- [x] `Serializable` 인터페이스는 제외합니다.  
- [x] 패키지명: `com.sprint.mission.discodeit.entity.base`  

- [x] JPA의 어노테이션을 활용해 `createdAt`, `updatedAt` 속성이 자동으로 설정되도록 구현하세요.  
  - `@CreatedDate`, `@LastModifiedDate`  

- [x] 클래스 다이어그램을 참고해 클래스 참조 관계를 수정하세요.  
  - 필요한 경우 생성자, update 메소드를 수정할 수 있습니다.  
  - 단, 아직 JPA Entity와 관련된 어노테이션은 작성하지 마세요.  

- [x] ERD와 클래스 다이어그램을 토대로 연관관계 매핑 정보를 표로 정리해보세요.  

| A       | B             | 다중성 | 방향성                                | 부모-자식 관계             | 연관관계의 주인 |
|---------|---------------|--------|--------------------------------------|----------------------------|----------------|
| User    | UserStatus    | 1:1    | 단방향 UserStatus → User              | 부모: UserStatus, 자식: User | UserStatus |
| User    | ReadStatus    | 1:N    | 단방향 ReadStatus → User              | 부모: ReadStatus, 자식: User | ReadStatus |
| User    | Message       | 1:N    | 단방향 Message → User (author)        | 부모: Message, 자식: User   | Message |
| User    | BinaryContent | 1:1(*) | 단방향 User → BinaryContent (profile) | 부모: User, 자식: BinaryContent | User |
| Channel | ReadStatus    | 1:N    | 단방향 ReadStatus → Channel           | 부모: ReadStatus, 자식: Channel | ReadStatus |
| Channel | Message       | 1:N    | 단방향 Message → Channel              | 부모: Message, 자식: Channel | Message |
| Message | BinaryContent | 1:N    | 단방향 Message → BinaryContent (attachment)| 부모: Message , 자식: BinaryContent | Message |

- [x] JPA 주요 어노테이션을 활용해 ERD, 연관관계 매핑 정보를 도메인 모델에 반영해보세요.  
  - `@Entity`, `@Table`  
  - `@Column`, `@Enumerated`  
  - `@OneToMany`, `@OneToOne`, `@ManyToOne`  
  - `@JoinColumn`, `@JoinTable`  

- [x] ERD의 외래키 제약 조건과 연관관계 매핑 정보의 부모-자식 관계를 고려해 영속성 전이와 고아 객체를 정의하세요.  
  - `cascade`, `orphanRemoval`  

---

## 레포지토리와 서비스에 JPA 도입하기

- [x] 기존의 Repository 인터페이스를 `JpaRepository`로 정의하고 쿼리 메소드로 대체하세요.  
- [x] `FileRepository`와 `JCFRepository` 구현체는 삭제합니다.  
- [x] 영속성 컨텍스트의 특징에 맞추어 서비스 레이어를 수정해보세요.  
  - 힌트: 트랜잭션, 영속성 전이, 변경 감지, 지연 로딩  

---

## DTO 적극 도입하기

- [x] Entity를 Controller 까지 그대로 노출했을 때 발생할 수 있는 문제점에 대해 정리해보세요. DTO를 적극 도입했을 때 보일러플레이트 코드가 많아지지만, 그럼에도 불구하고 어떤 이점이 있는지 알 수 있을거에요.
  - Entity와 API의 결합 방지  
  - OSIV = false 환경 대응  
  - 양방향 연관관계 시 순환 참조 방지  
  - 민감 데이터 보호
     
  **문제점**
  1. Controller에서 Entity를 JSON으로 변환할텐데 JoinColum에서 처럼 객체가 객체를 가지고 있게되면 계속해서 서로의 객체를 불러올거니 무한루프에 걸릴 수 있게 된다.
<img width="727" height="333" alt="{309259A5-3581-4DC4-9BA7-F020B1FA1F98}" src="https://github.com/user-attachments/assets/7d85284d-0280-41a8-b4d7-a55565614d0e" />
<img width="700" height="347" alt="{41CA36FD-B7F0-413E-996B-F2F97461303C}" src="https://github.com/user-attachments/assets/73c3bcfb-a01d-4c8c-9ee6-6d1526cf9d79" />
위 그림들 처럼 유저는 -> 메시지를 메시지는 -> 유저르 바라보았을 때 서로를 참조할려고 하기 때문에무한루프에 빠지게 된다.

  2. 엔티티 스펙이 노출된다.
    외부에서 손쉽게 필드값이 노출이 된다. 그러면 악의적인 접근이 들어올때 쉽게 노출이 되므로 보안상으로 큰 문제가 생긴다.

  3. 유지보수적으로 어렵다
    데이터 필드를 추가해야할때, Join을 추가해야할때 등 컨트롤러 쪽에서 바꾸면서 모든 기능을 변경해줘야한다.

  4. LazyInitializationException이 발생할 수 있다.
    우리가 Entity에 JPA를 도입할 때 지연로딩을 걸어주는 필드가 있을 것이다.
    그리고 서비스에서도 Transactional을 붙여 서비스가 메서드가 종료되었을 때 Hibernate의 Session이 함께 종료되어 영속성 컨텍스트가 사라진다.
    보통 지연로딩 (FetchType.LAZY)로 걸린 필드는 프록시 객체로 채워지고 이때 get을 통해 데이터를 가져오면 쿼리를 통해 실제 데이터로 채우게 되는데
    이때 서비스단에서 종료되어 트랜젝션이 종료되어 영속성 컨텍스트가 사라진 상태에 컨틀롤러로 넘어가게 되면 프록시 객체가 실제 객체를 채우지 못하는 경우가 생겨
    LazyInitializationException이 발생하게 된다.

- [x] 클래스 다이어그램을 참고하여 DTO를 정의하세요.  
- [x] Entity를 DTO로 매핑하는 로직을 책임지는 Mapper 컴포넌트를 정의해 반복되는 코드를 줄여보세요.  
  - 패키지명: `com.sprint.mission.discodeit.mapper`  

---

## BinaryContent 저장 로직 고도화

- [x] BinaryContent 엔티티는 파일의 메타 정보(`fileName`, `size`, `contentType`)만 표현하도록 `bytes` 속성을 제거하세요.  
- [x] BinaryContent의 `byte[]` 데이터 저장을 담당하는 인터페이스를 설계하세요.  
  - 패키지명: `com.sprint.mission.discodeit.storage`  

**BinaryContentStorage**
- `UUID put(UUID, byte[])` : 키(UUID) 기반 저장  
- `InputStream get(UUID)` : 키 기반 조회  
- `ResponseEntity<?> download(BinaryContentDto)` : 다운로드 응답 생성  

- [x] 서비스 레이어에서 기존 BinaryContent 저장 로직을 BinaryContentStorage로 리팩토링  
- [x] BinaryContentController에 파일 다운로드 API 추가  
  - 엔드포인트: `GET /api/binaryContents/{binaryContentId}/download`  
  - 요청: `binaryContentId` (Query Parameter)  
  - 응답: `ResponseEntity<?>`  

- [x] 로컬 디스크 저장 방식 구현체 작성  
  - `discodeit.storage.type = local` 일 때만 Bean 등록  
  - `discodeit.storage.local.root-path` 설정값 기반 저장  
  - 파일 저장 규칙: `{root}/{UUID}`  
  - `init()` 시 디렉토리 초기화  
  - `resolvePath(UUID)` 로 일관된 경로 유지  
  - `download()` 에서 ResponseEntity<Resource> 생성  

---

## 페이징과 정렬

- [x] 메시지 목록 조회 시 조건:  
  - **50개씩**  
  - **최신순**  
  - **총 개수 불필요**  

- [x] 일관된 페이지네이션 응답을 위해 제네릭 DTO 구현  
  - 패키지명: `com.sprint.mission.discodeit.dto.response`  
  - 구조:  
    - `content` : 실제 데이터  
    - `number` : 페이지 번호  
    - `size` : 페이지 크기  
    - `totalElements` : 총 개수 (nullable)  

- [x] `Slice` 또는 `Page` 객체로부터 DTO를 생성하는 Mapper 구현  
  - 패키지명: `com.sprint.mission.discodeit.mapper`  
  - 확장성을 위해 제네릭 메소드로 작성  

---

## 심화 요구사항

- [x] **N+1 문제**  
  - N+1 문제가 발생하는 쿼리를 찾고 해결  

- [x] **읽기 전용 트랜잭션 활용**  
  - OSIV(false) 환경에서 서비스 레이어 조회 시 발생 가능한 문제를 식별하고,  
    읽기 전용 트랜잭션(`@Transactional(readOnly = true)`) 활용  

```yaml
spring:
  jpa:
    open-in-view: false
````

* [x] **페이지네이션 최적화**

  * 오프셋 페이지네이션과 커서 페이지네이션 방식의 차이를 정리
  * 기존 오프셋 페이지네이션을 커서 페이지네이션으로 리팩토링
  * `PageResponse` 구조 변경

* [x] **MapStruct 적용**

  * Entity ↔ DTO 매핑 보일러플레이트 코드 간소화

```
