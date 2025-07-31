package com.sprint.mission.discodeit.repository.domain;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

@Repository("contentRepository")
public class BinaryContentRepositoryImpl implements BinaryContentRepository {
	@Override
	public void save(BinaryContent content) {

	}

	@Override
	public void deleteByOwnerId(UUID ownerId) {

	}
}
