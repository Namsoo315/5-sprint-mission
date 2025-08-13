package com.sprint.mission.discodeit.service.basic;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
	private final BinaryContentRepository binaryContentRepository;

	@Override
	public BinaryContent createBinaryContent(BinaryContentDTO request) {

		BinaryContent binaryContent = new BinaryContent(request.getFileName(), request.getContentType(), request.getSize(), request.getBinaryContent());
		binaryContentRepository.save(binaryContent);

		return binaryContent;
	}

	@Override
	public Optional<BinaryContent> findByBinaryContentId(UUID binaryContentId) {
		return binaryContentRepository.findById(binaryContentId);
	}

	@Override
	public List<BinaryContent> findAllByIdIn(List<UUID> attachmentIds) {
		return binaryContentRepository.findAllByIdIn(attachmentIds);
	}

	@Override
	public void delete(UUID binaryContentId) {
		binaryContentRepository.delete(binaryContentId);
	}
}
