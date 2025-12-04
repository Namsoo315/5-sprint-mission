# Spring Event - 파일 업로드 로직 분리하기

디스코드잇은 BinaryContent의 메타 데이터(DB)와 바이너리 데이터(FileSystem/S3)를 분리해 저장합니다.

만약 지금처럼 두 로직이 하나의 트랜잭션으로 묶인 경우 트랜잭션을 과도하게 오래 점유할 수 있는 문제가 있습니다.

바이너리 데이터 저장 연산은 오래 걸릴 수 있는 연산이며, 해당 연산이 끝날 때까지 트랜잭션이 대기해야합니다.
따라서 Spring Event를 활용해 메타 데이터 저장 트랜잭션으로부터 바이너리 데이터 저장 로직을 분리하여, 메타데이터 저장 트랜잭션이 종료되면 바이너리 데이터를 저장하도록
변경합니다.

---

## [x] BinaryContentStorage.put을 직접 호출하는 대신 BinaryContentCreatedEvent를 발행하세요.

### [x] BinaryContentCreatedEvent를 정의하세요.

BinaryContent 메타 정보가 DB에 잘 저장되었다는 사실을 의미하는 이벤트입니다.

### [x] 다음의 메소드에서 BinaryContentStorage를 호출하는 대신 BinaryContentCreatedEvent를 발행하세요.

* UserService.create/update
* MessageService.create
* BinaryContentService.create

ApplicationEventPublisher를 활용하세요.

---

## [x] 이벤트를 받아 실제 바이너리 데이터를 저장하는 리스너를 구현하세요.

* 이벤트를 발행한 메인 서비스의 트랜잭션이 커밋되었을 때 리스너가 실행되도록 설정하세요.
* BinaryContentStorage를 통해 바이너리 데이터를 저장하세요.

---

## [x] 바이너리 데이터 저장 성공 여부를 알 수 있도록 메타 데이터를 리팩토링하세요.

BinaryContent에 바이너리 데이터 업로드 상태 속성(status)을 추가하세요.

* PROCESSING: 업로드 중 (기본값)
* SUCCESS: 업로드 완료
* FAIL: 업로드 실패

### schema.sql

```sql
CREATE TABLE binary_contents
(
    id           uuid PRIMARY KEY,
    created_at   timestamp with time zone NOT NULL,
    updated_at   timestamp with time zone,
    file_name    varchar(255)             NOT NULL,
    size         bigint                   NOT NULL,
    content_type varchar(100)             NOT NULL,
    status       varchar(20)              NOT NULL
);
```

BinaryContent의 상태를 업데이트하는 메소드를 정의하세요.
트랜잭션 전파 범위에 유의하세요.

---

## [x] 바이너리 데이터 저장 성공 여부를 메타 데이터에 반영하세요.

* 성공 시 BinaryContent의 status를 SUCCESS로 업데이트하세요.
* 실패 시 BinaryContent의 status를 FAIL로 업데이트하세요.

<img width="751" height="783" alt="{35A0B64C-1C74-4863-91A8-D8F2AF2CDA58}" src="https://github.com/user-attachments/assets/cb41abc0-6612-4a97-94e8-2b950c70497c" />
Processing 일땐 이런식으로 뜨고 Success일땐 제대로 출려됨

---

알겠어. **요구한 그대로, 내용 수정 없이, 체크박스만 `[x]`로 변경**해서 README에 바로 복붙할 수 있는 버전으로 정리해줄게.
이미지 위치도 그대로 둘 테니 너가 직접 넣으면 돼.

---

# Spring Event - 알림 기능 추가하기

1) 채널에 새로운 메시지가 등록되거나 2) 권한이 변경된 경우 이벤트를 발행해 알림을 받을 수 있도록 구현합니다.

[x] 채널에 새로운 메시지가 등록된 경우 알림을 받을 수 있도록 리팩토링하세요.  
MessageCreatedEvent를 정의하고 새로운 메시지가 등록되면 이벤트를 발행하세요.

사용자 별로 관심있는 채널의 알림만 받을 수 있도록 ReadStatus 엔티티에 채널 알림 여부 속성(notificationEnabled)을 추가하세요.

PRIVATE 채널은 알림 여부를 true로 초기화합니다.  
PUBLIC 채널은 알림 여부를 false로 초기화합니다.

```sql
-- schema.sql
CREATE TABLE read_statuses
(
    id                   uuid PRIMARY KEY,
    created_at           timestamp with time zone NOT NULL,
    updated_at           timestamp with time zone,
    user_id              uuid                     NOT NULL,
    channel_id           uuid                     NOT NULL,
    last_read_at         timestamp with time zone NOT NULL,
    notification_enabled boolean                  NOT NULL,
    UNIQUE (user_id, channel_id)
);

-- ALTER TABLE read_statuses
--      ADD COLUMN notification_enabled boolean NOT NULL;
````

알림 여부를 수정할 수 있게 ReadStatusUpdateRequest를 수정하세요.

알림이 활성화 되어 있는 경우

알림이 활성화 되어 있지 않은 경우

[x] 사용자의 권한(Role)이 변경된 경우 알림을 받을 수 있도록 리팩토링하세요.
RoleUpdatedEvent를 정의하고 권한이 변경되면 이벤트를 발행하세요.

[x] 알림 API를 구현하세요.
NotificationDto를 정의하세요.

* receiverId: 알림을 수신할 User의 id입니다.

### 알림 조회

* 엔드포인트: `GET /api/notifications`
* 요청
  헤더: 엑세스 토큰
* 응답
  200 List<NotifcationDto>
  401 ErrorResponse

### 알림 확인

* 엔드포인트: `DELETE /api/notifications/{notificationId}`
* 요청
  헤더: 엑세스 토큰
* 응답
  204 Void
* 인증되지 않은 요청: 401 ErrorResponse
* 인가되지 않은 요청: 403 ErrorResponse
* 요청자 본인의 알림에 대해서만 수행할 수 있습니다.
* 알림이 없는 경우: 404 ErrorResponse

[x] 알림이 필요한 이벤트가 발행되었을 때 알림을 생성하세요.
이벤트를 처리할 리스너를 구현하세요.

```java
public class NotificationRequiredEventListener {

  @TransactionalEventListener
  public void on(MessageCreatedEvent event) {...}

  @TransactionalEventListener
  public void on(RoleUpdatedEvent event) {...}
}
```

### on(MessageCreatedEvent)

* 해당 채널의 알림 여부를 활성화한 ReadStatus를 조회합니다.
* 해당 ReadStatus의 사용자들에게 알림을 생성합니다.

#### 알림 예시

* title: "보낸 사람 (#채널명)"
* content: "메시지 내용"

단, 해당 메시지를 보낸 사람은 알림 대상에서 제외합니다.

### on(RoleUpdatedEvent)

* 권한이 변경된 당사자에게 알림을 생성합니다.

#### 알림 예시

* title: "권한이 변경되었습니다."
* content: "USER -> CHANNEL_MANAGER"

 잘 됩니다.
<img width="951" height="500" alt="{19EDD488-366E-443A-925A-673D121F1543}" src="https://github.com/user-attachments/assets/d02c20cb-6b8f-4c22-9e10-d8c340cfda80" />
    
--- 

