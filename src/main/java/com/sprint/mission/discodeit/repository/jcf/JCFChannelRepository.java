package com.sprint.mission.discodeit.repository.jcf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

public class JCFChannelRepository implements ChannelRepository {
	private final Map<UUID, Channel> map = new HashMap<>();

	public JCFChannelRepository() {
		Channel channel1 = new Channel("채널 1", "스터디 공부방입니다.");
		map.put(channel1.getChannelId(), channel1);

		Channel channel2 = new Channel("채널 2", "커뮤니티 노는방입니다.");
		map.put(channel2.getChannelId(), channel2);
	}

	@Override
	public Channel createChannel(String name, String description) {
		Channel channel = new Channel(name, description);
		map.put(channel.getChannelId(), channel);

		return channel;
	}

	@Override
	public Optional<Channel> findById(UUID uuid) {
		return Optional.ofNullable(map.get(uuid));
	}

	@Override
	public List<Channel> findByChannelName(String name) {
		List<Channel> list = new ArrayList<>();
		for (Channel channel : map.values()) {
			if (channel.getName().equals(name)) {
				list.add(channel);
			}
		}
		return list;
	}

	@Override
	public List<Channel> findByAllChannel() {
		return new ArrayList<>(map.values());
	}

	@Override
	public void updateChannel(UUID uuid, String name, String description) {
		for (Channel value : map.values()){
			if ( value.getChannelId().equals(uuid)){
				value.update(name, description);
			}
		}
	}

	@Override
	public void deleteChannel(UUID uuid) {
		map.remove(uuid);
	}
}
