package com.sprint.mission.discodeit.service.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

public class BasicChannelService implements ChannelService {
	private final ChannelRepository channelRepository;

	public BasicChannelService(ChannelRepository channelRepository) {
		this.channelRepository = channelRepository;
	}

	@Override
	public Channel createChannel(String name, String description) {
		Channel channel = new Channel(name, description);
		channelRepository.save(channel);

		return channel;
	}

	@Override
	public Optional<Channel> findByChannelId(UUID uuid) {
		return channelRepository.findById(uuid);
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
	public List<Channel> findAll() {
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
