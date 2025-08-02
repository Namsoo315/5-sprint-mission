package com.sprint.mission.discodeit.service.basic;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sprint.mission.discodeit.dto.userstatus.USCreateRequest;
import com.sprint.mission.discodeit.dto.userstatus.USUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;

import lombok.RequiredArgsConstructor;

@Service("userStatusService")
@RequiredArgsConstructor
public class BasicUSService implements UserStatusService {
	private final UserStatusRepository userStatusRepository;
	private final UserRepository userRepository;

	@Override
	public UserStatus createUserStatus(USCreateRequest request) {
		return null;
	}

	@Override
	public Optional<UserStatus> findById(UUID userStatusId) {
		return Optional.empty();
	}

	@Override
	public List<UserStatus> findAll() {
		return List.of();
	}

	@Override
	public UserStatus updateUserStatus(USUpdateRequest request) {
		return null;
	}

	@Override
	public UserStatus updateByUserid(UUID userId) {
		return null;
	}

	@Override
	public void delete(UUID userStatusId) {

	}
}
