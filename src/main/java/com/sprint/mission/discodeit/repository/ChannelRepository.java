package com.sprint.mission.discodeit.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

public interface ChannelRepository {
	Channel save(Channel channel);

	Optional<Channel> findById(UUID channelId);

	List<Channel> findAll();

	void delete(UUID channelId);

	boolean existsById(UUID channelId);
}
