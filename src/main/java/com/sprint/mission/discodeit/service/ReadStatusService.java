package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.readstatus.RSCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.RSUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;

public interface ReadStatusService {
	ReadStatus createReadStatus(RSCreateRequest request);

	Optional<ReadStatus> findById(UUID userStatusId);

	List<ReadStatus> findAllByUserId(UUID userId);

	ReadStatus updateReadStatus(RSUpdateRequest request);

	void delete(UUID userStatusId);
}
