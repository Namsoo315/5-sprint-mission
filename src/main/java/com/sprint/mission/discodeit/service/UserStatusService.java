package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.UserStatusDTO;
import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface UserStatusService {

  UserStatusDTO createUserStatus(UserStatusCreateRequest request);

  UserStatusDTO findByUserStatusId(UUID userStatusId);

  List<UserStatusDTO> findAll();

  UserStatusDTO updateUserStatus(UUID userStatusId, UserStatusUpdateRequest request);

  UserStatusDTO updateByUserId(UUID userId, UserStatusUpdateRequest request);

  void delete(UUID userStatusId);
}
