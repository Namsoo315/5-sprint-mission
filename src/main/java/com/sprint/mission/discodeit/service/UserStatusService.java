package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusDto;
import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;

public interface UserStatusService {

  UserStatusDto createUserStatus(UserStatusCreateRequest request);

  UserStatusDto findByUserStatusId(UUID userStatusId);

  List<UserStatusDto> findAll();

  UserStatusDto updateUserStatus(UUID userStatusId, UserStatusUpdateRequest request);

  UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request);

  void delete(UUID userStatusId);
}
