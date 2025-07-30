package com.sprint.mission.discodeit.service.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

public class BasicChannelService implements ChannelService {
	private final ChannelRepository repo;

	public BasicChannelService(ChannelRepository repo) {
		this.repo = repo;
	}

	@Override
	public Channel createChannel(String name, String description) {
		Channel channel = new Channel(name, description);
		repo.save(channel);

		return channel;
	}

	@Override
	public Optional<Channel> findByChannelId(UUID uuid) {
		return repo.findById(uuid);
	}

	@Override
	public List<Channel> findByChannelName(String name) {
		List<Channel> list = new ArrayList<>();
		for (Channel channel : repo.findAll()) {
			if (channel.getName().equals(name)) {
				list.add(channel);
			}
		}
		return list;
	}

	@Override
	public List<Channel> findAll() {
		return new ArrayList<>(repo.findAll());
	}

	@Override
	public void updateChannel(UUID uuid, String name, String description) {
		Channel channel = repo.findById(uuid).orElse(null);

		if(channel == null) {
			throw new IllegalArgumentException("유효한 ID 가 없습니다.");
		}
		channel.update(name, description);
		repo.save(channel);
	}

	@Override
	public void deleteChannel(UUID uuid) {
		repo.delete(uuid);
	}
}
