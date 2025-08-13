package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;

public interface ReadStatusService {
	ReadStatus createReadStatus(ReadStatusCreateRequest request);

	ReadStatus findByReadStatusId(UUID readStatusId);

	List<ReadStatus> findAllByUserId(UUID userId);

	ReadStatus updateReadStatus(ReadStatusUpdateRequest request);

	void delete(UUID userStatusId);
}
