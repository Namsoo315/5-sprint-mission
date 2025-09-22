package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import com.sprint.mission.discodeit.dto.data.UserDTO;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class BasicUserServiceTest {

  @InjectMocks
  private BasicUserService userService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private BinaryContentStorage binaryContentStorage;

  @Mock
  private BinaryContentRepository binaryContentRepository;

  @Mock
  private UserStatusRepository userStatusRepository;

  @Mock
  private UserMapper userMapper;

  @Mock
  private MultipartFile profile;

  private User user;                 // 매핑된 엔티티(저장 전)
  private BinaryContent binaryContent;
  private UserDTO userDTO; // 서비스 반환 DTO

  @BeforeEach
  void setUp() {
    user = User.builder()
        .id(UUID.randomUUID())
        .username("testUser")
        .email("test@example.com")
        .password("1234")
        .profile(null)
        .build();

    binaryContent = BinaryContent.builder()
        .id(UUID.randomUUID())
        .fileName("avatar.png")
        .contentType("image/png")
        .size(1024L)
        .build();

    userDTO = new UserDTO(user.getId(), "testUser", "test@example.com", null, true);


  }

  @Test
  @DisplayName("유저 생성 성공 ✅ (아바타 없음)")
  void createUser_success() {
    //given
    UserCreateRequest testUser = new UserCreateRequest("testUser", "test@example.com", "1234");

    given(userRepository.findByUsername("testUser")).willReturn(Optional.empty());
    given(userRepository.findByEmail("test@example.com")).willReturn(Optional.empty());
    given(userRepository.save(any(User.class))).willReturn(user);
    given(userMapper.toDto(user)).willReturn(userDTO);
    //when
    UserDTO result = userService.createUser(testUser, null);

    //then
    assertThat(result).isEqualTo(userDTO);
    then(userRepository).should(times(1)).save(any(User.class));
    then(userStatusRepository).should(times(1)).save(any(UserStatus.class));
    then(userMapper).should(times(1)).toDto(any(User.class));
  }

  @Test
  @DisplayName("유저 생성 성공 ✅ (아바타 있음)")
  void createUser_success_withAvatar() throws IOException {
    // given
    UserCreateRequest testUser = new UserCreateRequest("testUser", "test@example.com", "1234");

    // profile Mock 세팅
    given(profile.isEmpty()).willReturn(false);
    given(profile.getOriginalFilename()).willReturn("avatar.png");
    given(profile.getContentType()).willReturn("image/png");
    given(profile.getSize()).willReturn(1024L);
    given(profile.getBytes()).willReturn("fakeImage".getBytes());

    // repository, storage Mock 세팅
    given(userRepository.findByUsername("testUser")).willReturn(Optional.empty());
    given(userRepository.findByEmail("test@example.com")).willReturn(Optional.empty());
    given(binaryContentRepository.save(any(BinaryContent.class))).willReturn(binaryContent);

    // void 메소드 모킹
    given(binaryContentStorage.save(eq(binaryContent.getId()), any(byte[].class)))
        .willReturn(binaryContent.getId());

    given(userRepository.save(any(User.class))).willReturn(user);
    given(userMapper.toDto(user)).willReturn(userDTO);

    // when
    UserDTO result = userService.createUser(testUser, profile);

    // then
    assertThat(result).isEqualTo(userDTO);
    then(binaryContentRepository).should(times(1)).save(any(BinaryContent.class));
    then(binaryContentStorage).should(times(1)).save(any(UUID.class), any(byte[].class));
    then(userRepository).should(times(1)).save(any(User.class));
    then(userStatusRepository).should(times(1)).save(any(UserStatus.class));
    then(userMapper).should(times(1)).toDto(any(User.class));
  }

  @Test
  @DisplayName("❌ 유저 생성 실패 - 중복 username")
  void createUser_fail_duplicateUsername() {
    // given
    UserCreateRequest testUser = new UserCreateRequest("testUser", "test@example.com", "1234");
    given(userRepository.findByUsername("testUser")).willReturn(Optional.of(user));

    // when & then
    assertThatThrownBy(() -> userService.createUser(testUser, null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("같은 아이디가 존재합니다.");

    then(userRepository).should(times(0)).save(any(User.class));
    then(userStatusRepository).should(times(0)).save(any(UserStatus.class));
  }

  @Test
  @DisplayName("유저 정보 수정 성공 ✅ (아바타 없음)")
  void updateUser_success() throws IOException {
    // given
    UUID userId = user.getId();
    User updateTarget = User.builder()
        .id(userId)
        .username("oldUser")
        .email("old@example.com")
        .password("1234")
        .build();

    given(userRepository.findById(userId)).willReturn(Optional.of(updateTarget));
    given(userRepository.save(any(User.class))).willReturn(user);
    given(userMapper.toDto(user)).willReturn(userDTO);

    // when
    UserDTO result = userService.updateUser(userId,
        new UserUpdateRequest("testUser", "test@example.com", "1234"), null);

    // then
    assertThat(result).isEqualTo(userDTO);
    then(userRepository).should(times(1)).findById(userId);
    then(userRepository).should(times(1)).save(any(User.class));
    then(userMapper).should(times(1)).toDto(any(User.class));
  }

  @Test
  @DisplayName("유저 정보 수정 실패 ❌ - 존재하지 않는 회원")
  void updateUser_fail_notFound() {
    // given
    UUID userId = UUID.randomUUID();
    given(userRepository.findById(userId)).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> userService.updateUser(userId,
        new UserUpdateRequest("testUser", "test@example.com", "1234"), null))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessageContaining("존재하지 않는 회원입니다.");

    then(userRepository).should(times(1)).findById(userId);
    then(userRepository).should(never()).save(any(User.class));
    then(userMapper).should(never()).toDto(any(User.class));
  }


  @Test
  @DisplayName("유저 삭제 성공 ✅")
  void deleteUser_success() {
    // given
    UUID userId = user.getId();
    given(userRepository.findById(userId)).willReturn(Optional.of(user));

    // when
    userService.deleteUser(userId);

    // then
    then(userRepository).should(times(1)).deleteById(userId);
    then(userStatusRepository).should(times(1)).deleteByUserId(userId);
  }

  @Test
  @DisplayName("유저 삭제 실패 ❌ - 존재하지 않는 회원")
  void deleteUser_fail_notFound() {
    // given
    UUID userId = UUID.randomUUID();
    given(userRepository.findById(userId)).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> userService.deleteUser(userId))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessageContaining("존재하지 않는 회원입니다.");

    then(userRepository).should(times(1)).findById(userId);
    then(userRepository).should(never()).deleteById(any(UUID.class));
    then(userStatusRepository).should(never()).deleteByUserId(any(UUID.class));
  }

}