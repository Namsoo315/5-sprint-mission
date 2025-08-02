package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.binary.BICreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;

public interface BinaryContentService {
	BinaryContent createBinaryContent(BICreateRequest request);
	Optional<BinaryContent> findById(UUID binaryContentId);
	List<BinaryContent> findAllByIdIn(UUID[] binaryContentIds);		//Id 목록?
	void delete(UUID binaryContentId);
}
