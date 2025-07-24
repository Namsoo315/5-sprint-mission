package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
;

public interface ChannelService {
	Channel createChannel(String name, String description);

	Optional<Channel> findById(UUID uuid);

	List<Channel> findByChannelName(String name);

	List<Channel> findByAllChannel();

	void updateChannel(UUID uuid, String name, String description);

	void deleteChannel(UUID uuid);
}
