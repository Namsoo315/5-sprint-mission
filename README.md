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