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
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final UserStatusRepository userStatusRepository;
  private final UserMapper userMapper;

  @Override
  @Transactional
  public UserDTO createUser(UserCreateRequest userCreateRequest, MultipartFile profile)
      throws IOException {

    // 1. username, email 호환성 확인
    if (userRepository.findByUsername(userCreateRequest.username()).isPresent()) {
      throw new IllegalArgumentException("같은 아이디가 존재합니다.");
    }

    if (userRepository.findByEmail(userCreateRequest.email()).isPresent()) {
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
      binaryContentStorage.put(save.getId(), profile.getBytes());
    }

    // 3. user, userStatus 같이 생성.
    User result = User.builder()
        .username(userCreateRequest.username())
        .email(userCreateRequest.email())
        .password(userCreateRequest.password())
        .profile(content)
        .build();

    User save = userRepository.save(result);

    UserStatus userStatus = UserStatus.builder()
        .user(result)
        .lastActiveAt(Instant.now())
        .build();

    userStatusRepository.save(userStatus);

    return userMapper.toDto(save);
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

    BinaryContent savedContent = null;
    // 1. 선택적으로 프로필 이미지를 같이 등록함. (있으면 등록 없으면 등록 안함.)
    if (profile != null && !profile.isEmpty()) {
      BinaryContent content = BinaryContent.builder()
          .fileName(profile.getOriginalFilename())
          .contentType(profile.getContentType())
          .size(profile.getSize())
          .build();
      savedContent = binaryContentRepository.save(content);
      binaryContentStorage.put(savedContent.getId(), profile.getBytes());
    }

    // 2. User 호환성 체크
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다."));

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
    User save = userRepository.save(updatedUser);

    return userMapper.toDto(save);
  }

  @Override
  @Transactional(readOnly = true)
  public void deleteUser(UUID userId) {

    // 1. 관련 도메인도 같이 삭제 User, UserStatus, BinaryContent

    // 2. user 안에 있는 profileId -> BinaryContentId 삭제
    User user = userRepository.findById(userId).orElseThrow(
        () -> new NoSuchElementException("존재하지 않는 회원입니다."));

    if (user.getProfile() != null) {
      binaryContentRepository.delete(user.getProfile());
    }

    userStatusRepository.deleteByUserId(userId);
    userRepository.deleteById(userId);
  }
}
