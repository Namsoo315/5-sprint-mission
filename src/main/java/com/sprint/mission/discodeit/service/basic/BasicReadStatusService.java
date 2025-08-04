package com.sprint.mission.discodeit.service.basic;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;

import lombok.RequiredArgsConstructor;

@Service("readStatusService")
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
	private final ReadStatusRepository readStatusRepository;
	private final UserRepository userRepository;
	private final ChannelRepository channelRepository;

	@Override
	public ReadStatus createReadStatus(ReadStatusCreateRequest request) {
		// 관련된 Channel이나 User가 존재하지 않으면 예외를 발생시킵니다.
		if(userRepository.findById(request.getUserId()).isEmpty()){
			throw new IllegalArgumentException("유저가 존재하지 않습니다.");
		}

		if(channelRepository.findById(request.getChanelId()).isEmpty()){
			throw new IllegalArgumentException("채널이 존재하지 않습니다.");
		}

		// 같은 Channel과 User와 관련된 객체가 이미 존재하면 예외를 발생시킵니다.
		if(!readStatusRepository.findAllByUserId(request.getUserId()).isEmpty()){
			throw new IllegalArgumentException("이미 존재하는 유저가 있습니다.");
		}

		if(readStatusRepository.findAllByChannelId(request.getChanelId()).isEmpty()){
			throw new IllegalArgumentException("이미 존재하는 채널이 있습니다.");
		}


		ReadStatus result = new ReadStatus(request.getUserId(), request.getChanelId());
		readStatusRepository.save(result);

		return result;
	}

	@Override
	public Optional<ReadStatus> findById(UUID userStatusId) {
		return readStatusRepository.findById(userStatusId);
	}

	@Override
	public List<ReadStatus> findAllByUserId(UUID userId) {
		return readStatusRepository.findAllByUserId(userId);
	}

	@Override
	public ReadStatus updateReadStatus(ReadStatusUpdateRequest request) {
		ReadStatus readStatus = readStatusRepository.findById(request.getReadStatusId()).orElseThrow(
			() -> new IllegalArgumentException("상태 정보가 없습니다."));

		readStatus.update();	// 읽는 시간을 기준으로 업데이트? 필요하나?

		return readStatus;
	}

	@Override
	public void delete(UUID userStatusId) {
		readStatusRepository.delete(userStatusId);
	}
}
