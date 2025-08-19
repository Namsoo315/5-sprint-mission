package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

  ReadStatus createReadStatus(ReadStatusCreateRequest request);

  ReadStatus findByReadStatusId(UUID readStatusId);

  List<ReadStatus> findAllByUserId(UUID userId);

  ReadStatus updateReadStatus(UUID readStatusId, ReadStatusUpdateRequest request);

  void delete(UUID userStatusId);
}
