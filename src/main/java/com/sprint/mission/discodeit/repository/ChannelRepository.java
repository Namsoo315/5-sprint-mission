package com.sprint.mission.discodeit.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

public interface ChannelRepository {
	Channel save(Channel channel);

	Optional<Channel> findById(UUID id);

	List<Channel> findAll();

	long count();

	void delete(UUID id);

	boolean existsById(UUID id);
}
