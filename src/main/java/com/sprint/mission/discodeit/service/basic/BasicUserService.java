package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.data.UserDTO;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final UserStatusRepository userStatusRepository;
  private final UserMapper userMapper;

  @Override
  @Transactional
  public UserDTO createUser(UserCreateRequest userCreateRequest, MultipartFile profile) {

    // 1. username, email 호환성 확인
    if (userRepository.findByUsername(userCreateRequest.username()).isPresent()) {
      log.warn("이미 같은 아이디가 존재합니다. username={}", userCreateRequest.username());
      throw new IllegalArgumentException("같은 아이디가 존재합니다.");
    }

    if (userRepository.findByEmail(userCreateRequest.email()).isPresent()) {
      log.warn("이미 같은 이메일이 존재합니다. email={}", userCreateRequest.email());
      throw new IllegalArgumentException("같은 이메일이 존재합니다.");
    }

    BinaryContent content = null;
    // 2. 선택적으로 프로필 이미지를 같이 등록함. 있으면 등록 없으면 등록 안함.
    if (profile != null && !profile.isEmpty()) {
      content = BinaryContent.builder()
          .fileName(profile.getOriginalFilename())
          .contentType(profile.getContentType())
          .size(profile.getSize())
          .build();

      BinaryContent save = binaryContentRepository.save(content);
      try {
        binaryContentStorage.save(save.getId(), profile.getBytes());
      } catch (IOException e) {
        throw new UncheckedIOException("스토리지에 추가할 수 없습니다: " + e.getMessage(), e);
      }
    }

    // 3. user, userStatus 같이 생성.
    User result = User.builder()
        .username(userCreateRequest.username())
        .email(userCreateRequest.email())
        .password(userCreateRequest.password())
        .profile(content)
        .build();

    UserStatus userStatus = UserStatus.builder()
        .user(result)
        .lastActiveAt(Instant.now())
        .build();

    // log 추가
    log.info("생성할 유저의 아이디 ={}", result.getUsername());
    try {
      User save = userRepository.save(result);
      userStatusRepository.save(userStatus);

      log.debug("계정 생성 완료 ={}", save.getId());

      return userMapper.toDto(save);
    } catch (Exception e) {
      log.error("계정 생성에 실패하였습니다. ={}", result.getUsername(), e);
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  @Override
  @Transactional(readOnly = true)
  public UserDTO findByUserId(UUID userId) {
    // 1. 호환성 체크	user, userStatus Id(toDto가 함) 체크
    User save = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다."));
    return userMapper.toDto(save);
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserDTO> findAll() {
    List<User> saves = userRepository.findAll();
    return userMapper.toDto(saves);
  }

  @Override
  @Transactional
  public UserDTO updateUser(UUID userId, UserUpdateRequest userUpdateRequest,
      MultipartFile profile) throws IOException {
    // 1. User 호환성 체크
    User user = userRepository.findById(userId).orElseThrow(() -> {
      log.warn("존재하지 않는 회원 업데이트 시도 userId={}", userId);
      return new NoSuchElementException("존재하지 않는 회원입니다.");
    });

    BinaryContent savedContent = null;
    // 2. 선택적으로 프로필 이미지를 같이 등록함. (있으면 등록 없으면 등록 안함.)
    if (profile != null && !profile.isEmpty()) {
      BinaryContent content = BinaryContent.builder()
          .fileName(profile.getOriginalFilename())
          .contentType(profile.getContentType())
          .size(profile.getSize())
          .build();
      savedContent = binaryContentRepository.save(content);
      binaryContentStorage.save(savedContent.getId(), profile.getBytes());
    }

    // 3. Builder를 사용해서 profile 반영
    User updatedUser = user.toBuilder()
        .profile(savedContent != null ? savedContent : user.getProfile())
        .username(userUpdateRequest.newUsername() != null ? userUpdateRequest.newUsername()
            : user.getUsername())
        .email(
            userUpdateRequest.newEmail() != null ? userUpdateRequest.newEmail() : user.getEmail())
        .password(userUpdateRequest.newPassword() != null ? userUpdateRequest.newPassword()
            : user.getPassword())
        .build();

    log.info("업데이트할 유저의 아이디 ={}", updatedUser.getUsername());
    try {
      User save = userRepository.save(updatedUser);
      log.debug("업데이트된 유저의 아이디 ={}", save.getId());
      return userMapper.toDto(save);
    } catch (Exception e) {
      log.error("계정 업데이트에 실패하였습니다. ={}", user.getUsername(), e);
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  @Override
  @Transactional
  public void deleteUser(UUID userId) {
    log.info("계정을 삭제합니다. ={}", userId);
    User user = userRepository.findById(userId).orElseThrow(() -> {
      log.warn("존재하지 않는 회원 삭제 시도 userId={}", userId);
      return new NoSuchElementException("존재하지 않는 회원입니다.");
    });

    try {
      // 1. user 안에 있는 profile 삭제
      if (user.getProfile() != null) {
        binaryContentRepository.delete(user.getProfile());
      }
      // 2. 관련 도메인 삭제: UserStatus, User
      userStatusRepository.deleteByUserId(userId);
      userRepository.deleteById(userId);

      log.debug("계정 삭제 완료 username={}", user.getId());
    } catch (Exception e) {
      log.error("계정 삭제 실패 ", e);
      throw new IllegalArgumentException(e.getMessage());
    }
  }
}
