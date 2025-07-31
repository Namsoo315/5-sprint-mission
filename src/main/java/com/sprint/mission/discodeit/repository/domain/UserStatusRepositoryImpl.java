package com.sprint.mission.discodeit.repository.domain;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

@Repository("statusRepository")
public class UserStatusRepositoryImpl implements UserStatusRepository {
	@Override
	public void save(UserStatus status) {

	}

	@Override
	public Optional<UserStatus> findById(UUID statusId) {
		return Optional.empty();
	}

	@Override
	public void deleteByUserId(UUID userId) {

	}
}
