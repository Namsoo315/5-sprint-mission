아래는 **기존 내용을 그대로 유지하면서**, 체크박스만 **[ ] → [x]** 로 변경한 버전입니다.
README에 바로 붙여 넣어도 됩니다.

---

# 웹소켓 구현하기

[x] **웹소켓 환경 구성**
spring-boot-starter-websocket 의존성을 추가하세요.

```gradle
implementation 'org.springframework.boot:spring-boot-starter-websocket'
```

웹소켓 메시지 브로커 설정

```java

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {...
}
```

메모리 기반 SimpleBroker를 사용하세요.

```java

@Override
public void configureMessageBroker(MessageBrokerRegistry config) {...}
```

* SimpleBroker의 Destination Prefix는 **/sub** 으로 설정하세요.
  클라이언트에서 메시지를 구독할 때 사용합니다.
* Application Destination Prefix는 **/pub** 으로 설정하세요.
  클라이언트에서 메시지를 발행할 때 사용합니다.

```java

@Override
public void registerStompEndpoints(StompEndpointRegistry registry) {...}
```

* STOMP 엔드포인트는 **/ws** 로 설정하고,
  **SockJS 연결을 지원**해야 합니다.

---

[x] **메시지 송신**
첨부파일이 없는 단순 텍스트 메시지인 경우 STOMP를 통해 메시지를 전송할 수 있도록 컨트롤러를 구현하세요.

```java

@Controller
public class MessageWebSocketController {
    ...
  @MessageMapping(...)
}
```

* 클라이언트는 웹소켓으로 **/pub/messages** 엔드포인트에 메시지를 전송할 수 있어야 합니다.
* `@MessageMapping` 을 활용하세요.
* 메시지 전송 요청의 페이로드 타입은 **MessageCreateRequest** 를 그대로 활용합니다.
* 첨부파일이 포함된 메시지는 기존의 API **POST /api/messages** 를 그대로 활용합니다.

---

[x] **메시지 수신**

* 클라이언트는 채널 입장 시 웹소켓으로
  **/sub/channels.{channelId}.messages** 를 구독해 메시지를 수신합니다.

이를 고려해 메시지가 생성되면 해당 엔드포인트로 메시지를 보내는 컴포넌트를 구현하세요.

```java

@Component
public class WebSocketRequiredEventListener {
    ...
  private final SimpMessagingTemplate messagingTemplate;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleMessage(MessageCreatedEvent event) {...}
}
```

* MessageCreatedEvent를 통해 새로운 메시지 생성 이벤트를 확인하세요.
* SimpMessagingTemplate 를 통해 적절한 엔드포인트로 메시지를 전송하세요.

---

## 배포 아키텍처 구성하기

- [x]  다음의 다이어그램에 부합하는 배포 아키텍처를 Docker Compose를 통해 구현하세요.

---

## Reverse Proxy

Nginx 기반의 리버스 프록시 컨테이너를 구성하세요.

역할 및 설정은 다음과 같습니다:

* /api/*, /ws/* 요청은 Backend 컨테이너로 프록시 처리합니다.
* 이 외의 모든 요청은 정적 리소스(프론트엔드 빌드 결과)를 서빙합니다.
* 프론트엔드 정적 리소스는 Nginx 컨테이너 내부의 적절한 경로(/usr/share/nginx/html 등)에 복사하세요.
* 외부에서 접근 가능한 유일한 컨테이너이며, 3000번 포트를 통해 접근할 수 있어야 합니다.

---

## Backend

Spring Boot 기반의 백엔드 서버를 Docker 컨테이너로 구성하세요.

* Reverse Proxy를 통해 /api/*, /ws/* 요청이 이 서버로 전달됩니다.

---

## DB, Memory DB, Message Broker

Backend 컨테이너가 접근 가능한 다음의 인프라 컨테이너들을 구성하세요

* DB: PostgreSQL
* Memory DB: Redis
* Message Broker: Kafka

각 컨테이너는 Docker Compose 네트워크를 통해 백엔드에서 통신할 수 있어야 합니다.
외부 네트워크와 단절되어야 합니다.

---
