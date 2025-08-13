package com.sprint.mission.discodeit.service.basic;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sprint.mission.discodeit.dto.channel.ChannelFindResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
	private final ChannelRepository channelRepository;
	private final ReadStatusRepository readStatusRepository;
	private final MessageRepository messageRepository;

	@Override
	public Channel createPublicChannel(PublicChannelCreateRequest request) {
		// Public은 생성시 기존 로직을 유지.
		Channel channel = new Channel(ChannelType.PUBLIC, request.getName(), request.getDescription());
		channelRepository.save(channel);

		return channel;
	}

	@Override
	public Channel createPrivateChannel(PrivateChannelCreateRequest request) {
		// Private 채널 생성 로직 (name, description 은 생략함.)
		Channel channel = new Channel(ChannelType.PRIVATE, null, null);

		// 1. Channel에 참여하려는 User의 정보를 DTO를 통해서 받아 user 별 ReadStatus 정보를 생성한다.
		for (UUID userId : request.getParticipantsUserIds()) {
			ReadStatus readStatus = new ReadStatus(userId, channel.getChannelId());
			readStatusRepository.save(readStatus);
		}

		channelRepository.save(channel);

		return channel;
	}

	@Override
	public ChannelFindResponse findByChannelId(UUID channelId) {

		// 1. 호환성 확인
		Channel channel = channelRepository.findById(channelId).orElseThrow(
			() -> new IllegalArgumentException("존재하지 않는 채널입니다."));

		// 2. 해당 채널의 가장 최근 메시지의 시간 정보를 포함.
		Message latestMessageTime = messageRepository.latestMessageByChannelId(channelId)
			.orElseThrow(() -> new NoSuchElementException("일치하는 채널 아이디가 없습니다."));
		// 2-2. Private 채널인 경우 참여한 User의 Id 정보까지 포함시킴.
		List<UUID> participantsIds = new ArrayList<>();

		if (channel.getType() == ChannelType.PRIVATE) {
			participantsIds = readStatusRepository.findAllByChannelId(channelId).stream()
				.map(ReadStatus::getUserId)
				.toList();
		}

		// DTO를 통해
		return ChannelFindResponse.builder()
			.channelId(channelId)
			.type(channel.getType())
			.name(channel.getName())
			.description(channel.getDescription())
			.lastMessageTime(latestMessageTime.getCreatedAt())
			.participantsUserIds(participantsIds)
			.build();
	}

	@Override
	public List<ChannelFindResponse> findAllByUserId(UUID userId) {
		List<ChannelFindResponse> responses = new ArrayList<>();

		// 1. Private 채널은 User가 참여한 채널만 조회하기 위한 ChannelId를 List화 시킴.
		List<UUID> subscribeChannelIds = readStatusRepository.findAllByUserId(userId).stream()
			.map(ReadStatus::getChannelId)
			.toList();

		// 2. Public의 Channel도 보여줘야 한다.
		for (Channel channel : channelRepository.findAll()) {
			// ChannelType이 Public 경우와 User가 참여한 채널일 경우는 List에 넣게 된다.
			if (channel.getType() == ChannelType.PUBLIC
				|| channel.getType() == ChannelType.PRIVATE && subscribeChannelIds.contains(channel.getChannelId())) {

				// 3-1. 해당 채널의 가장 최근 메시지의 시간 정보를 포함시킴
				Message latestMessageTime = messageRepository.latestMessageByChannelId(channel.getChannelId())
					.orElseThrow(() -> new NoSuchElementException("일치하는 채널 아이디가 없습니다."));

				// 3-2. 그 채널의 참여한 모든 UserId를 불러옴.
				List<UUID> participantsIds = readStatusRepository.findAllByChannelId(channel.getChannelId()).stream()
					.map(ReadStatus::getUserId)
					.toList();

				// 4. DTO에 값 추가.
				responses.add(ChannelFindResponse.builder()
					.channelId(channel.getChannelId())
					.type(channel.getType())
					.name(channel.getName())
					.description(channel.getDescription())
					.lastMessageTime(latestMessageTime.getCreatedAt())
					.participantsUserIds(participantsIds)
					.build());
			}

		}

		return responses;
	}

	@Override
	public void updateChannel(ChannelUpdateRequest request) {
		// 1. 호환성 체크
		Channel channel = channelRepository.findById(request.getChannelId()).orElseThrow(
			() -> new IllegalArgumentException("채널을 조회할 수 없습니다."));

		// 1-2. PRIVATE일 경우 update 불가능
		if (channel.getType() == ChannelType.PRIVATE) {
			throw new IllegalArgumentException("PRIVATE 채널은 수정할 수 없습니다.");
		}

		// DTO를 통한 update
		channel.update(request.getName(), request.getDescription());
		channelRepository.save(channel);
	}

	@Override
	public void deleteChannel(UUID channelId) {

		// 관련 도메인 같이 삭제 Channel, Message, ReadStatus
		channelRepository.delete(channelId);

		messageRepository.deleteByChannelId(channelId);

		readStatusRepository.deleteByChannelId(channelId);
	}
}
