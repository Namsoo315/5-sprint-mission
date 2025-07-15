package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
;

public interface ChannelService {
	public Channel createChannel(String name, String description);
	public Optional<Channel> findById(UUID uuid);
	public List<Channel> findByChannelName(String name);
	public List<Channel> findByAllChannel();
	public void updateChannel(UUID uuid, String name, String description);
	public void deleteChannel(UUID uuid);
}
