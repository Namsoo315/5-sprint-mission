package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final UserStatusRepository userStatusRepository;

  @Override
  @Transactional
  public User createUser(UserCreateRequest userCreateRequest, BinaryContentDTO binaryContentDTO) {

    // 1. username, email 호환성 확인
    if (userRepository.findByUsername(userCreateRequest.username()).isPresent()) {
      throw new IllegalArgumentException("같은 아이디가 존재합니다.");
    }

    if (userRepository.findByEmail(userCreateRequest.email()).isPresent()) {
      throw new IllegalArgumentException("같은 이메일이 존재합니다.");
    }

    BinaryContent content = null;
    // 2. 선택적으로 프로필 이미지를 같이 등록함. 있으면 등록 없으면 등록 안함.
    if (binaryContentDTO != null &&
        binaryContentDTO.bytes() != null &&
        binaryContentDTO.bytes().length > 0) {

      content = BinaryContent.builder()
          .fileName(binaryContentDTO.fileName())
          .contentType(binaryContentDTO.contentType())
          .size(binaryContentDTO.size())
          .bytes(binaryContentDTO.bytes())
          .build();
      binaryContentRepository.save(content);
    }

    // 3. user, userStatus 같이 생성.
    User result = User.builder()
        .username(userCreateRequest.username())
        .email(userCreateRequest.email())
        .password(userCreateRequest.password())
        .profile(content)
        .build();

    userRepository.save(result);

    UserStatus userStatus = UserStatus.builder()
        .user(result)
        .lastActiveAt(Instant.now())
        .build();

    userStatusRepository.save(userStatus);

    return result;
  }

  @Override
  @Transactional
  public User findByUserId(UUID userId) {
    // 1. 호환성 체크	user, userStatus Id(toDto가 함) 체크
    return userRepository.findById(userId).orElseThrow(()
        -> new NoSuchElementException("존재하지 않는 회원입니다."));
  }

  @Override
  @Transactional
  public List<User> findAll() {
    return userRepository.findAll();
  }

  @Override
  @Transactional
  public User updateUser(UUID userId, UserUpdateRequest userUpdateRequest,
      BinaryContentDTO binaryContentDTO) {

    // 1. 선택적으로 프로필 이미지를 같이 등록함. (있으면 등록 없으면 등록 안함.)
    if (binaryContentDTO != null &&
        binaryContentDTO.bytes() != null &&
        binaryContentDTO.bytes().length > 0) {
      BinaryContent binaryContent = BinaryContent.builder()
          .fileName(binaryContentDTO.fileName())
          .contentType(binaryContentDTO.contentType())
          .size(binaryContentDTO.size())
          .bytes(binaryContentDTO.bytes())
          .build();
      binaryContentRepository.save(binaryContent);
    }

    // 2. User 호환성 체크
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다."));

    return userRepository.save(user);
  }

  @Override
  @Transactional
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
