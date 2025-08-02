package com.sprint.mission.discodeit.service.basic;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sprint.mission.discodeit.dto.binary.BICreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;

import lombok.RequiredArgsConstructor;

@Service("binaryContentService")
@RequiredArgsConstructor
public class BasicBIService implements BinaryContentService {
	private final BinaryContentRepository binaryContentRepository;
	@Override
	public BinaryContent createBinaryContent(BICreateRequest request) {
		return null;
	}

	@Override
	public Optional<BinaryContent> findById(UUID binaryContentId) {
		return Optional.empty();
	}

	@Override
	public List<BinaryContent> findAllByIdIn(UUID[] binaryContentIds) {
		return List.of();
	}

	@Override
	public void delete(UUID binaryContentId) {

	}
}
