package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;
  private final UserStatusMapper userStatusMapper;

  @Override
  public UserStatusDto createUserStatus(UserStatusCreateRequest request) {

    // 1. 호환성 체크: User가 존재하지 않으면 예외 처리
    User user = userRepository.findById(request.userId())
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));

    // 1-2. 같은 User와 관련된 UserStatus가 이미 존재하면 예외 처리
    userStatusRepository.findByUserId(request.userId())
        .ifPresent(us -> {
          throw new IllegalArgumentException("이미 존재하는 유저입니다.");
        });

    // 2. 유저 상태정보 저장.
    UserStatus userStatus = UserStatus.builder()
        .user(user)
        .lastActiveAt(request.lastActiveAt())
        .build();

    UserStatus save = userStatusRepository.save(userStatus);

    return userStatusMapper.toDto(save);
  }

  @Override
  @Transactional(readOnly = true)
  public UserStatusDto findByUserStatusId(UUID userStatusId) {
    UserStatus save = userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저정보입니다."));
    return userStatusMapper.toDto(save);
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserStatusDto> findAll() {
    List<UserStatus> saves = userStatusRepository.findAll();
    return userStatusMapper.toDto(saves);
  }

  @Override
  @Transactional
  public UserStatusDto updateUserStatus(UUID userStatusId, UserStatusUpdateRequest request) {
    // 1. 호환성 체크
    UserStatus userStatus = userStatusRepository.findById(userStatusId).orElseThrow(
        () -> new NoSuchElementException("존재하지 않는 유저정보입니다."));
    UserStatus save = userStatusRepository.save(userStatus);

    return userStatusMapper.toDto(save);
  }

  @Override
  public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request) {
    // 1. userId로 특정 UserStatus를 찾는 호환성 체크
    UserStatus userStatus = userStatusRepository.findByUserId(userId).orElseThrow(
        () -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    UserStatus save = userStatusRepository.save(userStatus);
    return userStatusMapper.toDto(save);
  }

  @Override
  @Transactional(readOnly = true)
  public void delete(UUID userStatusId) {
    if (!userStatusRepository.existsById(userStatusId)) {
      throw new NoSuchElementException("존재하지 않는 유저정보입니다.");
    }
    userStatusRepository.deleteById(userStatusId);
  }
}
