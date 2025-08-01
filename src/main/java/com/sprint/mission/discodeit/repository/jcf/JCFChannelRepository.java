package com.sprint.mission.discodeit.repository.jcf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

public class JCFChannelRepository implements ChannelRepository {
	private final Map<UUID, Channel> map = new HashMap<>();


	@Override
	public Channel save(Channel channel) {
		boolean isNew = !existsById(channel.getChannelId());

		map.put(channel.getChannelId(), channel);

		if (isNew) {
			System.out.println("생성 되었습니다.");
		} else {
			System.out.println("업데이트 되었습니다.");
		}
		return channel;
	}

	@Override
	public Optional<Channel> findById(UUID id) {
		if (existsById(id)) {
			return Optional.of(map.get(id));
		}

		return Optional.empty();
	}

	@Override
	public List<Channel> findAll() {
		return new ArrayList<>(map.values());
	}

	@Override
	public long count() {
		return map.size();
	}

	@Override
	public void delete(UUID id) {
		if (!existsById(id)) {
			throw new IllegalArgumentException("일치하는 ID가 없습니다.");
		}
		map.remove(id);
		System.out.println(id + " 유저가 삭제 되었습니다.");
	}

	@Override
	public boolean existsById(UUID id) {
		return map.containsKey(id);
	}
}
