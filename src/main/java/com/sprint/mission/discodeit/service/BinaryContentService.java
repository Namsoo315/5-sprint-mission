package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.binary.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;

public interface BinaryContentService {
	BinaryContent createBinaryContent(BinaryContentCreateRequest request);

	Optional<BinaryContent> findById(UUID binaryContentId);

	List<BinaryContent> findAllByIdIn(List<UUID> attachmentIds);

	void delete(UUID binaryContentId);
}
