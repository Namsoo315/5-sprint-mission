package com.sprint.mission.discodeit.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.BinaryContent;

public interface BinaryContentRepository {
	BinaryContent save(BinaryContent binaryContent);

	Optional<BinaryContent> findById(UUID binaryId);

	List<BinaryContent> findAll();

	List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds);

	void delete(UUID binaryId);

	void deleteByUserId(UUID userId);

	void deleteByAttachmentIds(List<UUID> attachmentIds);

	boolean existsById(UUID binaryId);
}
