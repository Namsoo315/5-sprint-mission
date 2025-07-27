# 기본 요구사항
## File IO를 통한 데이터 영속화
[x]  다음의 조건을 만족하는 서비스 인터페이스의 구현체를 작성하세요.

[x]  클래스 패키지명: com.sprint.mission.discodeit.service.file

[x]  클래스 네이밍 규칙: File[인터페이스 이름]

[x]  JCF 대신 FileIO와 객체 직렬화를 활용해 메소드를 구현하세요.

[x]  Application에서 서비스 구현체를 File*Service로 바꾸어 테스트해보세요.

***
## 서비스 구현체 분석
[x] JCF*Service 구현체와 File*Service 구현체를 비교하여 공통점과 차이점을 발견해보세요.

[x] "비즈니스 로직"과 관련된 코드를 식별해보세요.

[x] "저장 로직"과 관련된 코드를 식별해보세요.

## 레포지토리 설계 및 구현
[x] "저장 로직"과 관련된 기능을 도메인 모델 별 인터페이스로 선언하세요.

[x] 인터페이스 패키지명: com.sprint.mission.discodeit.repository

[x] 인터페이스 네이밍 규칙: [도메인 모델 이름]Repository

[x] 다음의 조건을 만족하는 레포지토리 인터페이스의 구현체를 작성하세요.

[x] 클래스 패키지명: com.sprint.mission.discodeit.repository.jcf

[x] 클래스 네이밍 규칙: JCF[인터페이스 이름]

[x] 기존에 구현한 JCF*Service 구현체의 "저장 로직"과 관련된 코드를 참고하여 구현하세요.

[x] 다음의 조건을 만족하는 레포지토리 인터페이스의 구현체를 작성하세요.

[x] 클래스 패키지명: com.sprint.mission.discodeit.repository.file

[x] 클래스 네이밍 규칙: File[인터페이스 이름]

[x] 기존에 구현한 File*Service 구현체의 "저장 로직"과 관련된 코드를 참고하여 구현하세요.

***
# 심화 요구 사항
## 관심사 분리를 통한 레이어 간 의존성 주입
[x] 다음의 조건을 만족하는 서비스 인터페이스의 구현체를 작성하세요.

[x] 클래스 패키지명: com.sprint.mission.discodeit.service.basic

[x] 클래스 네이밍 규칙: Basic[인터페이스 이름]

[x] 기존에 구현한 서비스 구현체의 "비즈니스 로직"과 관련된 코드를 참고하여 구현하세요.

[x] 필요한 Repository 인터페이스를 필드로 선언하고 생성자를 통해 초기화하세요.

[x] "저장 로직"은 Repository 인터페이스 필드를 활용하세요. (직접 구현하지 마세요.)

[x] Basic*Service 구현체를 활용하여 테스트해보세요.

***
# 코드 템플릿
```
public class JavaApplication {
static User setupUser(UserService userService) {
User user = userService.create("woody", "woody@codeit.com", "woody1234");
return user;
}
    static Channel setupChannel(ChannelService channelService) {
        Channel channel = channelService.create(ChannelType.PUBLIC, "공지", "공지 채널입니다.");
        return channel;
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        Message message = messageService.create("안녕하세요.", channel.getId(), author.getId());
        System.out.println("메시지 생성: " + message.getId());
    }

    public static void main(String[] args) {
        // 서비스 초기화
        // TODO Basic*Service 구현체를 초기화하세요.
        UserService userService;
        ChannelService channelService;
        MessageService messageService;

        // 셋업
        User user = setupUser(userService);
        Channel channel = setupChannel(channelService);
        // 테스트
        messageCreateTest(messageService, channel, user);
    }
}
```
***
[x]  JCF*Repository  구현체를 활용하여 테스트해보세요.

[x]  File*Repository 구현체를 활용하여 테스트해보세요.

[x] 이전에 작성했던 코드(JCF*Service 또는 File*Service)와 비교해 어떤 차이가 있는지 정리해보세요.
 + 저장로직과 비즈니스 로직을 합친 JCF와 각자의 역할을 분리한 FileService는 전체적인 가독성을 늘리게 하였고 특히나 DI자체의 결합도를 낮췄다고 생각함.

    [JCF*Service mainMenu]
<img width="731" height="129" alt="스크린샷 2025-07-27 오후 11 37 31" src="https://github.com/user-attachments/assets/a7037779-9bde-46f7-abd0-6e7349390b4d" />
    [File*Service mainMenu]
<img width="528" height="98" alt="image" src="https://github.com/user-attachments/assets/ca31d23a-3123-4e89-8955-8df5bd3dd87a" />

각자의 역할을 구분하였기 떄문에 직접 참조할 필요가 없이 각자의 로직에서만 변경점을 변경하면 되기 떄문에 좀 더 결합도를 낮추게 되었다
!하지만 이게 좋은 코드냐 라고 하면 그렇게 좋은 코드는 아니라고 생각한다. 스프링이후로 넘어가면 new 를 사용하지 않고 컴포넌트로 활용을 할 수 있기 때문에 더 결합도를 낮출 수 있게 될꺼라 생가함.
   

