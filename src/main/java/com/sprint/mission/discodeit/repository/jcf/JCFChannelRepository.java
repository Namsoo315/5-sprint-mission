package com.sprint.mission.discodeit.repository.jcf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

@Repository("channelRepository")
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
public class JCFChannelRepository implements ChannelRepository {
	private final Map<UUID, Channel> map = new HashMap<>();


	@Override
	public Channel save(Channel channel) {
		boolean isNew = !existsById(channel.getChannelId());

		map.put(channel.getChannelId(), channel);

		if (isNew) {
			System.out.println("channel이 생성 되었습니다.");
		} else {
			System.out.println("channel이 업데이트 되었습니다.");
		}
		return channel;
	}

	@Override
	public Optional<Channel> findById(UUID channelId) {
		if (existsById(channelId)) {
			return Optional.of(map.get(channelId));
		}

		return Optional.empty();
	}

	@Override
	public List<Channel> findAll() {
		return new ArrayList<>(map.values());
	}

	@Override
	public void delete(UUID channelId) {
		if (!existsById(channelId)) {
			throw new IllegalArgumentException("일치하는 ID가 없습니다.");
		}
		map.remove(channelId);
	}

	@Override
	public boolean existsById(UUID channelId) {
		return map.containsKey(channelId);
	}
}
