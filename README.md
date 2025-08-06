# 기본 요구사항
## Spring 프로젝트 초기화
 - [x] Spring Initializr를 통해 zip 파일을 다운로드하세요.
 - [x] Spring Initializr를 통해 zip 파일을 다운로드하세요.
 - [x] 빌드 시스템은 Gradle - Groovy를 사용합니다.
 - [x] 언어는 Java 17를 사용합니다.
 - [x] Spring Boot의 버전은 3.4.0입니다.
 - [x] GroupId는 com.sprint.mission입니다.
 - [x] ArtifactId와 Name은 discodeit입니다.
 - [x] packaging 형식은 Jar입니다
 - [x] Dependency를 추가합니다.
 - [x] Lombok
 - [x] Spring Web
 - [x] zip 파일을 압축해제하고 원래 진행 중이던 프로젝트에 붙여넣기하세요. 일부 파일은 덮어쓰기할 수 있습니다.
 - [x] application.properties 파일을 yaml 형식으로 변경하세요.
 - [x] DiscodeitApplication의 main 메서드를 실행하고 로그를 확인해보세요.
## Bean 선언 및 테스트
 - [x] File*Repository 구현체를 Repository 인터페이스의 Bean으로 등록하세요.
 - [x] Basic*Service 구현체를 Service 인터페이스의 Bean으로 등록하세요.
 - [x] JavaApplication에서 테스트했던 코드를 DiscodeitApplication에서 테스트해보세요.
 - [x]  JavaApplication 의 main 메소드를 제외한 모든 메소드를 DiscodeitApplication클래스로 복사하세요.
 - [x]  JavaApplication의 main 메소드에서 Service를 초기화하는 코드를 Spring Context를 활용하여 대체하세요.
 - 
```
// JavaApplication
public static void main(String[] args) {
    // 레포지토리 초기화
    // ...
    // 서비스 초기화
      UserService userService = new BasicUserService(userRepository);
    ChannelService channelService = new BasicChannelService(channelRepository);
    MessageService messageService = new BasicMessageService(messageRepository, channelRepository, userRepository);

    // ...
}

// DiscodeitApplication
public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);
    // 서비스 초기화
        // TODO context에서 Bean을 조회하여 각 서비스 구현체 할당 코드 작성하세요.
      UserService userService;
    ChannelService channelService;
    MessageService messageService;

    // ...
}
```
### JavaApplication의 main 메소드의 셋업, 테스트 부분의 코드를 DiscodeitApplication클래스로 복사하세요.
```
public static void main(String[] args) {
    // ...
    // 셋업
    User user = setupUser(userService);
    Channel channel = setupChannel(channelService);
    // 테스트
    messageCreateTest(messageService, channel, user);
}
```
## Spring 핵심 개념 이해하기
 - [x] JavaApplication과 DiscodeitApplication에서 Service를 초기화하는 방식의 차이에 대해 다음의 키워드를 중심으로 정리해보세요.
+ IoC Container
    + 객체의 생성, 생명주기 관리, 의존성 주입을 Spring에서 직접 관리하게 하였다.

+ Dependency Injection
    + 객체 간의 의존관계를 직접 생성하는 대신, 외부 컨테이너에서 객체를 생성하여 결합도를 낮춤.
+ Bean
    + 원래라면 Bean을 로직들에 주입하고 Configuration을 통한 Bean 사용을 정해야 하지만 IoC를 통해 스프링이 관리하여 서비스 위주 개발을 진행할 수 있게 되었다.에 첨부해주세요.

## Lombok 적용
 - [x] 도메인 모델의 getter 메소드를 @Getter로 대체해보세요.
 - [x] Basic*Service의 생성자를 @RequiredArgsConstructor로 대체해보세요.

# 추가 기능 요구 사항

## 시간 타입 변경하기
 - [x] 시간을 다루는 필드의 타입은 Instant로 통일합니다.
**기존에 사용하던 Long보다 가독성이 뛰어나며, 시간대(Time Zone) 변환과 정밀한 시간 연산이 가능해 확장성이 높습니다.**

# 새로운 도메인 추가하기
**도메인 모델 간 참조 관계를 참고하세요.**
<img width="600" height="600" alt="image" src="https://github.com/user-attachments/assets/cec0e445-6613-477b-985e-9ed8a9e95ae0" />

 - [x]  공통: 앞서 정의한 도메인 모델과 동일하게 공통 필드(id, createdAt, updatedAt)를 포함합니다.

### ReadStatus

사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현하는 도메인 모델입니다. 사용자별 각 채널에 읽지 않은 메시지를 확인하기 위해 활용합니다.

### UserStatus

사용자 별 마지막으로 확인된 접속 시간을 표현하는 도메인 모델입니다. 사용자의 온라인 상태를 확인하기 위해 활용합니다.

 - [x] 마지막 접속 시간을 기준으로 현재 로그인한 유저로 판단할 수 있는 메소드를 정의하세요.

마지막 접속 시간이 현재 시간으로부터 5분 이내이면 현재 접속 중인 유저로 간주합니다.

### BinaryContent
이미지, 파일 등 바이너리 데이터를 표현하는 도메인 모델입니다. 사용자의 프로필 이미지, 메시지에 첨부된 파일을 저장하기 위해 활용합니다.

 - [x] 수정 불가능한 도메인 모델로 간주합니다. 따라서 updatedAt 필드는 정의하지 않습니다.
 - [x] User, Message 도메인 모델과의 의존 관계 방향성을 잘 고려하여 id 참조 필드를 추가하세요.
 - [x]  각 도메인 모델 별 레포지토리 인터페이스를 선언하세요.

레포지토리 구현체(File, JCF)는 아직 구현하지 마세요. 이어지는 서비스 고도화 요구사항에 따라 레포지토리 인터페이스에 메소드가 추가될 수 있어요.

# 고도화

## UserService 고도화
### create
 - [x] 선택적으로 프로필 이미지를 같이 등록할 수 있습니다.
 - [x] DTO를 활용해 파라미터를 그룹화합니다.
 - 유저를 등록하기 위해 필요한 파라미터, 프로필 이미지를 등록하기 위해 필요한 파라미터 등
 - [x] username과 email은 다른 유저와 같으면 안됩니다.
 - [x] UserStatus를 같이 생성합니다.
### find, findAll
 - DTO를 활용하여:
 - [x] 사용자의 온라인 상태 정보를 같이 포함하세요.
 - [x] 패스워드 정보는 제외하세요.
### update
 - [x] 선택적으로 프로필 이미지를 대체할 수 있습니다.
 - [x] DTO를 활용해 파라미터를 그룹화합니다.
 - 수정 대상 객체의 id 파라미터, 수정할 값 파라미터
### delete
 - [x] 관련된 도메인도 같이 삭제합니다.
 - **BinaryContent(프로필), UserStatus**
### 의존성
같은 레이어 간 의존성 주입은 순환 참조 방지를 위해 지양합니다. 다른 Service 대신 필요한 Repository 의존성을 주입해보세요.

## AuthService 구현
### login
 - [x] username, password과 일치하는 유저가 있는지 확인합니다.
 - [x] 일치하는 유저가 있는 경우: 유저 정보 반환
 - [x] 일치하는 유저가 없는 경우: 예외 발생
 - [x] DTO를 활용해 파라미터를 그룹화합니다.
### 의존성
같은 레이어 간 의존성 주입은 순환 참조 방지를 위해 지양합니다. 다른 Service 대신 필요한 Repository 의존성을 주입해보세요.
<p align="center">
    <img width="300" height="500" alt="image" src="https://github.com/user-attachments/assets/5558a970-a690-4c6f-98fd-3e05452fead2" />
</p>


## ChannelService 고도화

### create
 - PRIVATE 채널과 PUBLIC 채널을 생성하는 메소드를 분리합니다.
 - [x] 분리된 각각의 메소드를 DTO를 활용해 파라미터를 그룹화합니다.
 - PRIVATE 채널을 생성할 때:
 - [x] 채널에 참여하는 User의 정보를 받아 User 별 ReadStatus 정보를 생성합니다.
 - [x] name과 description 속성은 생략합니다.
 - PUBLIC 채널을 생성할 때에는 기존 로직을 유지합니다.
### find
 - DTO를 활용하여:
 - [x] 해당 채널의 가장 최근 메시지의 시간 정보를 포함합니다.
 - [x] PRIVATE 채널인 경우 참여한 User의 id 정보를 포함합니다.
### findAll
 - DTO를 활용하여:
 - [x] 해당 채널의 가장 최근 메시지의 시간 정보를 포함합니다.
 - [x] PRIVATE 채널인 경우 참여한 User의 id 정보를 포함합니다.
 - [x] 특정 User가 볼 수 있는 Channel 목록을 조회하도록 조회 조건을 추가하고, 메소드 명을 변경합니다. 
### findAllByUserId
 - [x] PUBLIC 채널 목록은 전체 조회합니다.
 - [x] PRIVATE 채널은 조회한 User가 참여한 채널만 조회합니다.
### update
 - [x] DTO를 활용해 파라미터를 그룹화합니다.
 - 수정 대상 객체의 id 파라미터, 수정할 값 파라미터
 - [x] PRIVATE 채널은 수정할 수 없습니다.
### delete
 - [x] 관련된 도메인도 같이 삭제합니다.
 - **Message, ReadStatus**

### 의존성
같은 레이어 간 의존성 주입은 순환 참조 방지를 위해 지양합니다. 다른 Service 대신 필요한 Repository 의존성을 주입해보세요.
<img width="600" height="300" alt="image" src="https://github.com/user-attachments/assets/3535dc4b-429c-43c5-a475-6e98e246ae12" />


## MessageService 고도화
### create
 - [x] 선택적으로 여러 개의 첨부파일을 같이 등록할 수 있습니다.
 - [x] DTO를 활용해 파라미터를 그룹화합니다.
### findAll
 - [x] 특정 Channel의 Message 목록을 조회하도록 조회 조건을 추가하고, 메소드 명을 변경합니다.        
 - **findallByChannelId**
### update
 - [x] DTO를 활용해 파라미터를 그룹화합니다.
 - 수정 대상 객체의 id 파라미터, 수정할 값 파라미터
### delete
 - [x] 관련된 도메인도 같이 삭제합니다.
 - 첨부파일(BinaryContent)

### 의존성
같은 레이어 간 의존성 주입은 순환 참조 방지를 위해 지양합니다. 다른 Service 대신 필요한 Repository 의존성을 주입해보세요.
<img width="700" height="352" alt="image" src="https://github.com/user-attachments/assets/27105cca-3521-4729-9b72-b73aecafe7ee" />

## ReadStatusService 구현
### create
 - [x] DTO를 활용해 파라미터를 그룹화합니다.
 - [x] 관련된 Channel이나 User가 존재하지 않으면 예외를 발생시킵니다.
 - [x] 같은 Channel과 User와 관련된 객체가 이미 존재하면 예외를 발생시킵니다.
### find
 - [x] id로 조회합니다.
### findAllByUserId
 - [x] userId를 조건으로 조회합니다.
### update
 - [x] DTO를 활용해 파라미터를 그룹화합니다.
 - 수정 대상 객체의 id 파라미터, 수정할 값 파라미터
### delete
 - [x] id로 삭제합니다.
### 의존성
같은 레이어 간 의존성 주입은 순환 참조 방지를 위해 지양합니다. 다른 Service 대신 필요한 Repository 의존성을 주입해보세요.

<img width="600" height="300" alt="image" src="https://github.com/user-attachments/assets/94e70b79-b4bb-408c-bfd3-e698d7737f5e" />


## UserStatusService 고도화
### create
 - [x] DTO를 활용해 파라미터를 그룹화합니다.
 - [x] 관련된 User가 존재하지 않으면 예외를 발생시킵니다.
 - [x] 같은 User와 관련된 객체가 이미 존재하면 예외를 발생시킵니다.
### find
 - [x] id로 조회합니다.
### findAll
 - [x] 모든 객체를 조회합니다.
### update
 - [x] DTO를 활용해 파라미터를 그룹화합니다.
 - 수정 대상 객체의 id 파라미터, 수정할 값 파라미터
### updateByUserId
 - [x] userId 로 특정 User의 객체를 업데이트합니다.
### delete
 - [x] id로 삭제합니다.
### 의존성
같은 레이어 간 의존성 주입은 순환 참조 방지를 위해 지양합니다. 다른 Service 대신 필요한 Repository 의존성을 주입해보세요.
<img width="600" height="400" alt="image" src="https://github.com/user-attachments/assets/cfbce163-d6f0-4950-9587-e1b12e285002" />

## BinaryContentService 구현
### create
 - [x] DTO를 활용해 파라미터를 그룹화합니다.
### find
 - [x] id로 조회합니다.
### findAllByIdIn
 - [x] id 목록으로 조회합니다.
### delete
 - [x] id로 삭제합니다.
### 의존성
같은 레이어 간 의존성 주입은 순환 참조 방지를 위해 지양합니다. 다른 Service 대신 필요한 Repository 의존성을 주입해보세요.
<img width="500" height="450" alt="image" src="https://github.com/user-attachments/assets/4f89810d-df9d-4bad-8312-b7211df95925" />

# 새로운 도메인 Repository 구현체 구현
 - [x]  지금까지 인터페이스로 설계한 각각의 Repository를 JCF, File로 각각 구현하세요.
<img width="600" height="400" alt="image" src="https://github.com/user-attachments/assets/1f90d402-f5e1-48e4-bb9e-79f7870329b1" />

# DTO란?
> DTO(Data Transfer Object)란 데이터를 전달하기 위한 단순한 객체를 말합니다.
이어지는 요구사항을 해결하려다보면, 도메인 모델의 일부 정보만 포함하거나, 여러 도메인 모델의 정보를 합친 데이터 모델이 필요한 경우가 생길거예요.
이런 경우에 도메인 모델을 특정 상황만을 위해 수정하기 보다는 DTO를 정의해서 해결하는 것이 바람직합니다.
뿐만 아니라 메소드 파라미터가 많아지거나, 그룹핑하고 싶을 때에도 유용할 수 있어요.

**DTO를 정의할 때에는 class 대신 Record를 활용하면 더 편리합니다.**

```
public record MyDto(
        UUID id,
        String prop1,
        Long prop2
) {}

public record MyCreateRequest(
                String param1,
                Long param2
) {}

public MyDto create(MyCreateRequest request) {...}
```


# 심화 요구사항
## Bean 다루기
 - [x]  Repository 구현체 중에 어떤 구현체를 Bean으로 등록할지 Java 코드의 변경 없이 application.yaml 설정 값을 통해 제어해보세요.
```
# application.yaml
discodeit:
    repository: 
        type: jcf   # jcf | file
```
 - [x] discodeit.repository.type 설정값에 따라 Repository 구현체가 정해집니다.
 - [x] 값이 jcf 이거나 없으면 JCF*Repository 구현체가 Bean으로 등록되어야 합니다.
 - [x] 값이 file 이면 File*Repository 구현체가 Bean으로 등록되어야 합니다.
 - [x]  File*Repository 구현체의 파일을 저장할 디렉토리 경로를 application.yaml 설정 값을 통해 제어해보세요.
```
# application.yaml
discodeit:
    repository: 
        type: jcf   # jcf | file
        file-directory: .discodeit
```
