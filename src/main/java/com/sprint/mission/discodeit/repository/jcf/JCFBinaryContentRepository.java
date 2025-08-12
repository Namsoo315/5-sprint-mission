package com.sprint.mission.discodeit.repository.jcf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

@Repository("binaryContentRepository")
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
public class JCFBinaryContentRepository implements BinaryContentRepository {
	private final Map<UUID, BinaryContent> map = new HashMap<>();

	@Override
	public BinaryContent save(BinaryContent binaryContent) {
		boolean isNew = !existsById(binaryContent.getBinaryContentId());

		map.put(binaryContent.getBinaryContentId(), binaryContent);

		if (isNew) {
			System.out.println("binaryContent가 생성 되었습니다.");
		} else {
			System.out.println("binaryContent가 업데이트 되었습니다.");
		}
		return binaryContent;
	}

	@Override
	public Optional<BinaryContent> findById(UUID binaryId) {
		if (existsById(binaryId)) {
			return Optional.of(map.get(binaryId));
		}

		return Optional.empty();
	}

	@Override
	public List<BinaryContent> findAll() {
		return new ArrayList<>(map.values());
	}

	@Override
	public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
		List<BinaryContent> result = new ArrayList<>();

		for (UUID binaryContentId : binaryContentIds) {
			result.add(map.get(binaryContentId));
		}

		return result;
	}

	@Override
	public void delete(UUID binaryId) {
		if (!existsById(binaryId)) {
			throw new IllegalArgumentException("일치하는 ID 가 없습니다.");
		}
		map.remove(binaryId);
	}

	@Override
	public void deleteByAttachmentId(List<UUID> attachmentIds) {
		for(UUID binaryId : attachmentIds) {
			if (!existsById(binaryId)) {
				throw new IllegalArgumentException("일치하는 ID 가 없습니다.");
			}
			map.remove(binaryId);
		}
	}

	@Override
	public boolean existsById(UUID binaryId) {
		return map.containsKey(binaryId);
	}
}
