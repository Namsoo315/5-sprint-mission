package com.sprint.mission.discodeit.service.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sprint.mission.discodeit.dto.channel.ChannelFindResponse;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelCreateResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.domain.ReadStatusRepositoryImpl;
import com.sprint.mission.discodeit.service.ChannelService;

import lombok.RequiredArgsConstructor;

@Service("channelService")
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
	private final ChannelRepository channelRepository;
	private final UserRepository userRepository;
	private final ReadStatusRepository statusRepository;
	private final ReadStatusRepositoryImpl readStatusRepository;

	@Override
	public Channel createPublicChannel(PublicChannelCreateRequest request) {
		Channel channel = new Channel(ChannelType.PUBLIC, request.getName(), request.getDescription());
		channelRepository.save(channel);

		return channel;
	}

	@Override
	public ChannelCreateResponse createPrivateChannel(PrivateChannelCreateRequest request) {
		Channel channel = new Channel(ChannelType.PRIVATE, request.getName(), request.getDescription());
		channelRepository.save(channel);

		for (UUID userId : request.getParticipantsUserIds()) {
			Optional<User> userOptional = userRepository.findById(userId);
			if (userOptional.isEmpty()) {
				throw new IllegalArgumentException("존재하지 않는 유저 입니다.");
			}

			ReadStatus readStatus = new ReadStatus(userOptional.get().getUserId(), channel.getChannelId());
			statusRepository.save(readStatus);
		}
		/*
		PRIVATE 채널을 생성할 때:
		[ ] 채널에 참여하는 User의 정보를 받아 User 별 ReadStatus 정보를 생성합니다.
		[ ] name과 description 속성은 생략합니다.
		*/

		return ChannelCreateResponse.builder()
			.channelId(channel.getChannelId())
			.channelType(channel.getType())
			.build();
	}

	@Override
	public Optional<ChannelFindResponse> findByChannelId(UUID channelId) {
		Channel channel = channelRepository.findById(channelId).orElseThrow(
			() -> new IllegalArgumentException("존재하지 않는 채널입니다."));

		List<UUID> participantsIds = new ArrayList<>();
		if (channel.getType() == ChannelType.PRIVATE) {
			participantsIds = readStatusRepository.findUserIdsByChannelId(channelId);
		}

		return Optional.ofNullable(ChannelFindResponse.builder()
				.channelId(channelId)
				.type(channel.getType())
				.name(channel.getName())
				.description(channel.getDescription())
				.participantsUserIds(participantsIds)		// 시간 최신화 추가해야함.
				.build());

	}

	@Override
	public List<Channel> findByChannelName(String name) {
		List<Channel> list = new ArrayList<>();
		for (Channel channel : channelRepository.findAll()) {
			if (channel.getName().equals(name)) {
				list.add(channel);
			}
		}
		return list;
	}

	@Override
	public List<Channel> findAllByUserId(UUID userId) {
		return new ArrayList<>(channelRepository.findAll());
	}

	@Override
	public void updateChannel(UUID uuid, String name, String description) {
		Channel channel = channelRepository.findById(uuid).orElse(null);

		if(channel == null) {
			throw new IllegalArgumentException("유효한 ID 가 없습니다.");
		}
		channel.update(name, description);
		channelRepository.save(channel);
	}

	@Override
	public void deleteChannel(UUID uuid) {
		channelRepository.delete(uuid);
	}
}
