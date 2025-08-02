package com.sprint.mission.discodeit.service.basic;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sprint.mission.discodeit.dto.channel.ChannelFindResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import lombok.RequiredArgsConstructor;

@Service("channelService")
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
	private final ChannelRepository channelRepository;
	private final ReadStatusRepository readStatusRepository;
	private final MessageRepository messageRepository;

	@Override
	public Channel createPublicChannel(PublicChannelCreateRequest request) {
		Channel channel = new Channel(ChannelType.PUBLIC, request.getName(), request.getDescription());
		channelRepository.save(channel);

		return channel;
	}

	@Override
	public Channel createPrivateChannel(PrivateChannelCreateRequest request) {
		Channel channel = new Channel(ChannelType.PRIVATE, null, null);
		channelRepository.save(channel);

		for (UUID userId : request.getParticipantsUserIds()) {
			ReadStatus readStatus = new ReadStatus(userId, channel.getChannelId());
			readStatusRepository.save(readStatus);
		}

		return channel;
	}

	@Override
	public Optional<ChannelFindResponse> findByChannelId(UUID channelId) {
		Channel channel = channelRepository.findById(channelId).orElseThrow(
			() -> new IllegalArgumentException("존재하지 않는 채널입니다."));

		Instant latestMessageTime = messageRepository.LatestMessageByChannelId(channelId);

		List<ReadStatus> participantsIds = new ArrayList<>();
		if (channel.getType() == ChannelType.PRIVATE) {
			participantsIds = readStatusRepository.findAllByChannelId(channelId);
		}

		return Optional.ofNullable(ChannelFindResponse.builder()
			.channelId(channelId)
			.type(channel.getType())
			.name(channel.getName())
			.description(channel.getDescription())
			.lastMessageTime(latestMessageTime)
			.participantsUserIds(participantsIds)
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
	public List<ChannelFindResponse> findAllByUserId(UUID userId) {
		List<UUID> subscribeChannelIds = readStatusRepository.findAllByUserId(userId).stream()
			.map(ReadStatus::getChannelId)
			.toList();

		List<ChannelFindResponse> responses = new ArrayList<>();

		for (Channel channel : channelRepository.findAll()) {
			if (channel.getType() == ChannelType.PUBLIC ||
				channel.getType() == ChannelType.PRIVATE && subscribeChannelIds.contains(channel.getChannelId())) {
				Instant latestMessageTime = messageRepository.LatestMessageByChannelId(channel.getChannelId());
				List<ReadStatus> allByChannelId = readStatusRepository.findAllByChannelId(channel.getChannelId());

				responses.add(ChannelFindResponse.builder()
					.channelId(channel.getChannelId())
					.type(channel.getType())
					.name(channel.getName())
					.description(channel.getDescription())
					.lastMessageTime(latestMessageTime)
					.participantsUserIds(allByChannelId)
					.build());
			}

		}

		return responses;
	}

	@Override
	public void updateChannel(ChannelUpdateRequest request) {
		Channel channel = channelRepository.findById(request.getChannelId()).orElseThrow(
			() -> new IllegalArgumentException("채널을 조회할 수 없습니다."));

		if (channel.getType() == ChannelType.PRIVATE) {
			throw new IllegalArgumentException("PRIVATE 채널은 수정할 수 없습니다.");
		}

		channel.update(request.getName(), request.getDescription());
		channelRepository.save(channel);
	}

	@Override
	public void deleteChannel(UUID channelId) {
		channelRepository.delete(channelId);

		messageRepository.deleteByChannelId(channelId);

		readStatusRepository.deleteByChannelId(channelId);
	}
}
