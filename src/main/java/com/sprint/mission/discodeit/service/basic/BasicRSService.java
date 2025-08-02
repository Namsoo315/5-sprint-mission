package com.sprint.mission.discodeit.service.basic;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sprint.mission.discodeit.dto.readstatus.RSCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.RSUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;

import lombok.RequiredArgsConstructor;

@Service("readStatusService")
@RequiredArgsConstructor
public class BasicRSService implements ReadStatusService {
	private final ReadStatusRepository readStatusRepository;
	private final UserRepository userRepository;
	private final ChannelRepository channelRepository;

	@Override
	public ReadStatus createReadStatus(RSCreateRequest request) {
		return null;
	}

	@Override
	public Optional<ReadStatus> findById(UUID userStatusId) {
		return Optional.empty();
	}

	@Override
	public List<ReadStatus> findAllByUserId(UUID userId) {
		return List.of();
	}

	@Override
	public ReadStatus updateReadStatus(RSUpdateRequest request) {
		return null;
	}

	@Override
	public void delete(UUID userStatusId) {

	}
}
