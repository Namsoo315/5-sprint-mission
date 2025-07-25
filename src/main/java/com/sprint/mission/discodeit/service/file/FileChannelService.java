package com.sprint.mission.discodeit.service.file;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

public class FileChannelService implements ChannelService {
	private final ChannelRepository repo;

	public FileChannelService(ChannelRepository repo) {
		this.repo = repo;
	}

	@Override
	public Channel createChannel(String name, String description) {
		Channel channel = new Channel(name, description);
		repo.save(channel);

		return channel;
	}

	@Override
	public Optional<Channel> findById(UUID uuid) {
		return repo.findById(uuid);
	}

	@Override
	public List<Channel> findByChannelName(String name) {
		List<Channel> list = new ArrayList<>();

		for (Channel channel : repo.findAll()){
			if (channel.getName().equals(name)){
				list.add(channel);
			}
		}

		return list;
	}

	@Override
	public List<Channel> findByAllChannel() {
		return repo.findAll();
	}

	@Override
	public void updateChannel(UUID uuid, String name, String description) {
		Channel channel = repo.findById(uuid).orElse(null);

		if(channel == null) {
			throw new IllegalArgumentException("채널이 존재하지 않습니다.");
		}

		channel.setName(name);
		channel.setDescription(description);
		repo.save(channel);
	}

	@Override
	public void deleteChannel(UUID uuid) {
		repo.delete(uuid);
	}
}
