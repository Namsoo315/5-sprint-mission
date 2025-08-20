package com.sprint.mission.discodeit.service.basic;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;

  @Override
  public UserStatus createUserStatus(UserStatusCreateRequest request) {

    // 1. 호환성 체크 User 가 존재하지 않으면 예외 처리
    if (userRepository.findById(request.userId()).isEmpty()) {
      throw new IllegalArgumentException("존재하지 않는 유저입니다.");
    }

    // 1-2. 같은 User와 관련된 객체가 UserStatus에 이미 존재하면 예외 처리
    if (userStatusRepository.findByUserId(request.userId()).isPresent()) {
      throw new IllegalArgumentException("이미 존재하는 유저입니다.");
    }

    // 2. 유저 상태정보 저장.
    UserStatus userStatus = new UserStatus(request.userId(), request.lastActiveAt());
    userStatusRepository.save(userStatus);

    return userStatus;
  }

  @Override
  public UserStatus findByUserStatusId(UUID userStatusId) {
    return userStatusRepository.findById(userStatusId).orElseThrow(
        () -> new NoSuchElementException("일치하는 userStatusId가 없습니다."));
  }

  @Override
  public List<UserStatus> findAll() {
    return userStatusRepository.findAll();
  }

  @Override
  public UserStatus updateUserStatus(UUID userStatusId, UserStatusUpdateRequest request) {
    // 1. 호환성 체크
    UserStatus userStatus = userStatusRepository.findById(userStatusId).orElseThrow(
        () -> new IllegalArgumentException("존재하지 않는 유저 상태 정보입니다."));

    // 2. 유저 상태정보 업데이트
    userStatus.updateStatus(request.newLastActiveAt());
    return userStatusRepository.save(userStatus);
  }

  @Override
  public UserStatus updateByUserId(UUID userId, UserStatusUpdateRequest request) {
    // 1. userId로 특정 UserStatus를 찾는 호환성 체크
    UserStatus userStatus = userStatusRepository.findByUserId(userId).orElseThrow(
        () -> new IllegalArgumentException("존재하지 않는 유저 입니다."));

    // 2. 유저 상태정보 업데이트
    userStatus.updateStatus(request.newLastActiveAt());
    return userStatusRepository.save(userStatus);
  }

  @Override
  public void delete(UUID userStatusId) {
    userStatusRepository.delete(userStatusId);
  }
}
