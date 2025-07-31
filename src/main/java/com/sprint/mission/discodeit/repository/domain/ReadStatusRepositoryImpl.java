package com.sprint.mission.discodeit.repository.domain;

import org.springframework.stereotype.Repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

@Repository("readStatusRepository")
public class ReadStatusRepositoryImpl implements ReadStatusRepository {
	@Override
	public void save(ReadStatus status) {

	}
}
