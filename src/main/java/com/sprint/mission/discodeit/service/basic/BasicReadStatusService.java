package com.sprint.mission.discodeit.service.basic;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
	private final ReadStatusRepository readStatusRepository;
	private final UserRepository userRepository;
	private final ChannelRepository channelRepository;

	@Override
	public ReadStatus createReadStatus(ReadStatusCreateRequest request) {

		// 1. 호환성 체크 User, Channel 체크
		User user = userRepository.findById(request.userId()).orElseThrow(
			() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

		Channel channel = channelRepository.findById(request.channelId()).orElseThrow(
			() -> new IllegalArgumentException("채널이 존재하지 않습니다."));

		// 1-2. 만약 channelId와 userId가 매치되는 readStatus가 존재하면 예외 처리
		Optional<ReadStatus> existing = readStatusRepository.findByUserIdAndChannelId(user.getUserId(),
			channel.getChannelId());

		if (existing.isPresent()) {
			throw new IllegalArgumentException("이미 같은 channelId와 UserId가 존재하는 readStatus가 있습니다.");
		}

		// 2. 생성
		ReadStatus readStatus = new ReadStatus(user.getUserId(), channel.getChannelId(), request.lastReadAt());
		readStatusRepository.save(readStatus);

		return readStatus;
	}

	@Override
	public ReadStatus findByReadStatusId(UUID readStatusId) {
		return readStatusRepository.findByReadStatusId(readStatusId).orElseThrow(
			() -> new NoSuchElementException("일치하는 ReadStatusId가 없습니다."));
	}

	@Override
	public List<ReadStatus> findAllByUserId(UUID userId) {
		return readStatusRepository.findAllByUserId(userId);
	}

	@Override
	public ReadStatus updateReadStatus(ReadStatusUpdateRequest request) {
		Instant newLastReadAt = request.lastReadAt();
		// 1. 호환성 체크
		ReadStatus readStatus = readStatusRepository.findByReadStatusId(request.readStatusId()).orElseThrow(
			() -> new IllegalArgumentException("상태 정보가 없습니다."));

		// 2. 상태정보 업데이트 후 Repository save(update)
		readStatus.update(newLastReadAt);
		readStatusRepository.save(readStatus);

		return readStatus;
	}

	@Override
	public void delete(UUID userStatusId) {
		readStatusRepository.delete(userStatusId);
	}
}
